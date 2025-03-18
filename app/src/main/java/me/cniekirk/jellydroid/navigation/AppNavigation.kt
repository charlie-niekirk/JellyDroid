package me.cniekirk.jellydroid.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.R
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultExit
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopExit
import me.cniekirk.jellydroid.core.model.MediaType
import me.cniekirk.jellydroid.feature.home.mobile.Home
import me.cniekirk.jellydroid.feature.home.mobile.home
import me.cniekirk.jellydroid.feature.mediacollection.CollectionType
import me.cniekirk.jellydroid.feature.mediacollection.MediaCollection
import me.cniekirk.jellydroid.feature.mediadetails.MediaDetails
import me.cniekirk.jellydroid.feature.mediadetails.mediaDetails
import me.cniekirk.jellydroid.feature.mediaplayer.MediaPlayer
import me.cniekirk.jellydroid.feature.mediaplayer.mediaPlayer
import me.cniekirk.jellydroid.feature.onboarding.Onboarding
import me.cniekirk.jellydroid.feature.onboarding.onboardingUserJourney
import timber.log.Timber

@Composable
fun JellydroidNavHost(modifier: Modifier = Modifier, navHostController: NavHostController) {
    val bottomBarNavController = rememberNavController()

    NavHost(modifier = modifier, navController = navHostController, startDestination = Onboarding) {
        onboardingUserJourney(navHostController) {
            navHostController.navigate(MainApp) {
                launchSingleTop = true
                popUpTo(MainApp) {
                    inclusive = true
                }
            }
        }
        composable<MainApp>(
            enterTransition = { activityDefaultEnter() },
            exitTransition = { activityDefaultExit() },
            popEnterTransition = { activityDefaultPopEnter() }
        ) {
            MainBottomBarNavigation(
                navHostController = bottomBarNavController,
                navigateToPlayer = { navHostController.navigate(MediaPlayer(it)) }
            )
        }

        mediaPlayer()
    }
}

@Serializable
data object MainApp

@Serializable
data object Favorites

@Serializable
data object Library

@Serializable
data object Downloads

data class BottomNavRoute<T : Any>(
    val name: String,
    val route: T,
    val icon: ImageVector
)

@Composable
fun MainBottomBarNavigation(
    navHostController: NavHostController,
    navigateToPlayer: (String) -> Unit
) {
    val bottomNavRoutes = listOf(
        BottomNavRoute(stringResource(R.string.bottom_nav_home), Home, Icons.Default.Home),
        BottomNavRoute(stringResource(R.string.bottom_nav_favorites), Favorites, Icons.Default.Favorite),
        BottomNavRoute(stringResource(R.string.bottom_nav_library), Library, Icons.Default.VideoLibrary),
        BottomNavRoute(stringResource(R.string.bottom_nav_downloads), Downloads, Icons.Default.Download),
    )

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
        currentWindowAdaptiveInfo()
    )

    NavigationSuiteScaffoldLayout(
        navigationSuite = {
            if (navSuiteType == NavigationSuiteType.NavigationRail) {
                NavigationRail {
                    Spacer(Modifier.weight(1f))
                    bottomNavRoutes.forEach { item ->
                        NavigationRailItem(
                            icon = { Icon(item.icon, contentDescription = item.icon.name) },
                            label = { Text(item.name) },
                            selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                            onClick = {
                                navHostController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navHostController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )

                        Spacer(Modifier.height(8.dp))
                    }
                    Spacer(Modifier.weight(1f))
                }
            } else {
                NavigationSuite {
                    bottomNavRoutes.forEach { item ->
                        item(
                            icon = { Icon(item.icon, contentDescription = item.icon.name) },
                            label = { Text(item.name) },
                            selected = currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true,
                            onClick = {
                                navHostController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navHostController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        NavHost(navController = navHostController, startDestination = bottomNavRoutes.first().route) {
            home(
                onUserViewClicked = { id, name ->
                    navHostController.navigate(MediaCollection(id, name))
                },
                onResumeItemClicked = {},
                onMediaItemClicked = { id, name ->
                    navHostController.navigate(MediaDetails(id, name))
                }
            )
            mediaDetails(
                onPlayClicked = { navigateToPlayer(it) },
                onBackClicked = { navHostController.popBackStack() }
            )
        }
    }
}