# Jellydroid - Android client for [Jellyfin](https://jellyfin.org/)

## About

Jellydroid is an open source Android client for the Jellyfin media server. It started out as a personal project that I could use to try out new technologies and best practices, but I think now would be a great time to open source it & see what ideas the wider community has.

It is built with as many new/modern technologies as possible:

- Multi-module approach with core/feature modules
- 100% Jetpack Compose
- Dagger/Hilt for DI
- Utilizes some very alpha/pre-alpha libs including Navigation3 & Material 3 Expressive
- Build logic is consolidated in re-usable binary gradle convention plugins

## Contributing

All contributions, however small, are welcomed! There are just a few "good to know's" before you start. 

- If you add/change any logic, please create/update the appropriate unit tests.
- If you make changes to a feature module, add it's label to your PR so that the appropriate unit tests are executed.
- Once your PR is created, a release build will be generated and added to the PR in a comment, please use this APK to smoke test your changes in a release build.

### Architecture

- The app is built with modularity in mind.
- The `:core:domain` module is a plain kotlin module and I'd like to keep it this way. `:core:data` & `:feature:xyz` modules all depend on `:core:domain`.
- `:core:domain` provides repository interface definitions, use cases that can be shared between modules & the domain models. Feature modules consume the domain layer's use cases & sometimes repositories, whereas the data module consumes domain to provide implementations and bindings for the repository interfaces. 

Compose compiler metrics can be enabled by adding this to any of the modules' `build.gradle.kts`:
```kotlin
composeConvention {
    compilerMetricsEnabled.set(true)
}
```