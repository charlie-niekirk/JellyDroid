# **A Developer's Guide to Testing Android Gradle Convention Plugins**

Creating convention plugins is a powerful way to share build logic, enforce standards, and simplify configuration across your Android project's modules. However, testing them involves interacting with Gradle's complex internals, which can lead to a unique set of challenges.

This guide summarizes a real-world debugging journey, outlining common pitfalls and their robust solutions when using GradleRunner for testing.

### **The Goal: A Testable Convention Plugin**

Our objective was to test a convention plugin, AsosHiltConventionPlugin, which applies the Hilt and KSP plugins and adds their dependencies. The final, correct plugin code looks like this:

**`build-logic/convention/src/main/kotlin/.../AsosHiltConventionPlugin.kt`**
```kotlin
class AsosHiltConventionPlugin : Plugin<Project> {  
    override fun apply(target: Project) {  
        with(target) {  
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // STEP 1: Apply all necessary plugins FIRST to create the configurations.  
            pluginManager.apply(libs.findPlugin("hilt-plugin").get().get().pluginId)  
            pluginManager.apply(libs.findPlugin("ksp-plugin").get().get().pluginId)

            // STEP 2: Now that 'implementation' and 'ksp' exist, add dependencies.  
            dependencies {  
                add("implementation", libs.findLibrary("hilt-core").get())  
                add("ksp", libs.findLibrary("hilt-compiler").get())  
            }  
        }  
    }  
}
```

## **The 5 Common Pitfalls & Their Solutions**

When testing a plugin like the one above, we encountered and solved five distinct issues. Here they are, framed as pitfalls to watch out for.

### **Pitfall \#1: The test fails because it can't find libs.versions.toml.**

* **Problem:** Your test's settings.gradle.kts needs to locate your main project's version catalog. Hardcoded relative paths (../../../) are brittle, and System.getProperty("user.dir") is unreliable in composite builds (like build-logic).
* **Solution:** Programmatically search upwards from the test execution directory for a root project marker, like settings.gradle.kts. This makes your test setup robust and independent of where it's run.

**In your ...Test.kt file:**
```kotlin
private fun findRootProjectDir(): File {  
    var currentDir = File(System.getProperty("user.dir"))  
    while (!File(currentDir, "settings.gradle.kts").exists()) {  
        currentDir = currentDir.parentFile ?: throw IllegalStateException("Root project dir not found")  
    }  
    return currentDir  
}

@Before  
fun setup() {  
    // ...  
    val rootDir = findRootProjectDir()  
    val tomlPath = File(rootDir, "gradle/libs.versions.toml").absolutePath.replace('\\', '/')  
    val settingsTemplate = // ... read settings template from resources  
    settingsFile.writeText(settingsTemplate.replace("__PLACEHOLDER__", tomlPath))  
    // ...  
}
```

### **Pitfall \#2: The plugin under test is "Not Found".**

* **Problem:** The GradleRunner test environment is isolated. It doesn't know about your convention plugin's code, or you might be using the wrong identifier for it.
* **Solution:**
    1. **Always use .withPluginClasspath()** in your GradleRunner chain. This injects your build-logic module's compiled code into the test's classpath.
    2. **Use the correct ID.** If you have formally registered your plugin with a custom ID in your build-logic/build.gradle.kts (using a gradlePlugin { ... } block), you **must** use that ID (e.g., "asos.convention.hilt"). If you have *not* formally registered it, you must use its fully qualified class name (e.g., "com.asos.convention.AsosHiltConventionPlugin").

### **Pitfall \#3: A *dependent* plugin is "Not Found" (e.g., Hilt, KSP).**

* **Problem:** Your convention plugin tries to apply(plugin \= "com.google.dagger.hilt.android"), but the test fails because that plugin can't be found.
* **Solution:** Your convention plugin module needs a **build-time dependency** on any plugin it applies. The plugin's code must be on its classpath (the "plumber's truck" must have the tools). Add the required plugins as implementation dependencies in your convention plugin module's build.gradle.kts.

**In build-logic/convention/build.gradle.kts:**
```kotlin
dependencies {  
    // Make the Hilt and KSP plugin code available to our convention plugin  
    implementation(libs.hilt.dependency)  
    implementation(libs.ksp.dependency)

    // Other compile-time dependencies  
    compileOnly(libs.android.gradle.plugin)  
    compileOnly(libs.kotlin.gradle.plugin)  
}
```

*(This assumes you have corresponding \[libraries\] entries in your libs.versions.toml for hilt-dependency and ksp-dependency.)*

### **Pitfall \#4: "Configuration 'implementation' Not Found".**

* **Problem:** You try to add a dependency (dependencies.add("implementation", ...)), but the build fails because the implementation configuration (the "bucket") doesn't exist yet.
* **Solution:** Follow the Golden Rule: **Apply plugins first, then configure them.** Inside your convention plugin's apply method, ensure all pluginManager.apply(...) calls are executed *before* you open the dependencies { ... } block. Applying the Android, Kotlin, or Java plugins is what creates the implementation configuration.

### **Pitfall \#5: "The Hilt Android Gradle plugin can only be applied to an Android Project."**

* **Problem:** After solving the previous issues, the build now fails because the Hilt plugin has a safety check. Your test project is still a blank slate and not a valid Android project.
* **Solution:** Configure the test project to be a realistic Android module. In the test's build.gradle.kts content, apply the base Android plugin and provide its minimal required configuration *before* applying your convention plugin.

**Content for the test's build.gradle.kts:**
```kotlin
plugins {  
    // 1\. Make this an Android project  
    alias(libs.plugins.androidLibrary)

    // 2\. NOW apply your convention plugin  
    id("asos.convention.hilt")  
}

// 3\. Provide minimal Android config  
android {  
    namespace = "com.example.test.project"  
    compileSdk = 34  
}
```

## **Conclusion**

The central lesson is that **your test environment must accurately reflect the real-world conditions your plugin will face.** This involves managing classpaths, respecting the order of operations, and building a project that meets the prerequisites of every plugin in the chain. By systematically addressing these layers, you can create robust and reliable tests for any convention plugin.