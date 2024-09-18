package me.cniekirk.jellydroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.navigation.JellydroidNavHost

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JellyDroidTheme {
                Surface {
                    val navHostController = rememberNavController()
                    JellydroidNavHost(navHostController = navHostController)
                }
            }
        }
    }
}