package me.cniekirk.jellydroid.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuite
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.R
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultExit
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopEnter
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.navigation.AppBackStack
import me.cniekirk.jellydroid.core.navigation.routes.TopLevelRoute
import me.cniekirk.jellydroid.feature.home.mobile.Home
import me.cniekirk.jellydroid.feature.home.mobile.home
import me.cniekirk.jellydroid.feature.mediacollection.CollectionType
import me.cniekirk.jellydroid.feature.mediacollection.MediaCollection
import me.cniekirk.jellydroid.feature.mediacollection.mediaCollection
import me.cniekirk.jellydroid.feature.mediadetails.MediaDetailsRoute
import me.cniekirk.jellydroid.feature.mediadetails.mediaDetails
import me.cniekirk.jellydroid.feature.mediaplayer.MediaPlayer
import me.cniekirk.jellydroid.feature.mediaplayer.mediaPlayer
import me.cniekirk.jellydroid.feature.onboarding.Onboarding
import me.cniekirk.jellydroid.feature.onboarding.landing
import me.cniekirk.jellydroid.feature.onboarding.landing.LandingRoute
import me.cniekirk.jellydroid.feature.onboarding.landing.LandingViewModel
import me.cniekirk.jellydroid.feature.onboarding.onboardingUserJourney
import me.cniekirk.jellydroid.feature.onboarding.serverselection.ServerSelectionRoute
import me.cniekirk.jellydroid.feature.onboarding.serverselection.ServerSelectionViewModel
import me.cniekirk.jellydroid.feature.settings.Settings
import me.cniekirk.jellydroid.feature.settings.settings

@Serializable
data object Landing : NavKey

@Serializable
data object ServerSelection : NavKey

@Serializable
data object Settings : NavKey

@Serializable
data object Favourites : TopLevelRoute {
    override val icon = Icons.Default.Star
    override val name = R.string.bottom_nav_favorites
}

@Serializable
data object Library : TopLevelRoute {
    override val icon = Icons.Default.VideoLibrary
    override val name = R.string.bottom_nav_library
}

@Serializable
data object Downloads : TopLevelRoute {
    override val icon = Icons.Default.Download
    override val name = R.string.bottom_nav_downloads
}

@Composable
fun NewJellydroidNavHost(modifier: Modifier) {
    val appBackstack = remember {
        AppBackStack<Any>(startKey = Landing)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (appBackstack.isTopLevelOrTabRoute) {
                NavigationBar {

                }
            }
        }
    ) { contentPadding ->
        NavDisplay(
            backStack = appBackstack.backStack,
            onBack = { appBackstack.removeLast() },
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                landing(
                    navigateToHome = { appBackstack.add(Home) },
                    navigateToServerSelection = { appBackstack.add(ServerSelection) }
                )
                entry<ServerSelection> {
                    val viewModel = viewModel<ServerSelectionViewModel>()
                    ServerSelectionRoute(
                        viewModel = viewModel,
                        navigateToLogin = { serverName ->
                            appBackstack.add(Login(serverName))
                        }
                    )
                }
                entry<Login> { key ->

                }
                home(
                    onUserViewClicked = { id, name, kind ->
                        val type = when (kind) {
                            CollectionKind.MOVIES -> CollectionType.MOVIES
                            CollectionKind.SERIES -> CollectionType.SERIES
                        }
//                            navHostController.navigate(MediaCollection(id, name, type))
                    },
                    onResumeItemClicked = {},
                    onMediaItemClicked = { id, name ->
//                            navHostController.navigate(MediaDetailsRoute(id, name))
                    },
                    navigateToSettings = { appBackstack.add(me.cniekirk.jellydroid.navigation.Settings) }
                )
                entry<Favourites> {

                }
                entry<Library> {

                }
                entry<Downloads> {

                }
            },
            modifier = Modifier.consumeWindowInsets(contentPadding)
        )
    }
}

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
                navigateToPlayer = { navHostController.navigate(MediaPlayer(it)) },
                navigateToSettings = { navHostController.navigate(Settings) }
            )
        }

        mediaPlayer()

        settings(
            onBackPressed = { navHostController.popBackStack() }
        )
    }
}

@Serializable
data object MainApp

@Serializable
data object Favorites

//@Serializable
//data object Library

//@Serializable
//data object Downloads

data class BottomNavRoute<T : Any>(
    val name: String,
    val route: T,
    val icon: ImageVector
)

@Composable
fun MainBottomBarNavigation(
    navHostController: NavHostController,
    navigateToPlayer: (String) -> Unit,
    navigateToSettings: () -> Unit
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
                onUserViewClicked = { id, name, kind ->
                    val type = when (kind) {
                        CollectionKind.MOVIES -> CollectionType.MOVIES
                        CollectionKind.SERIES -> CollectionType.SERIES
                    }
                    navHostController.navigate(MediaCollection(id, name, type))
                },
                onResumeItemClicked = {},
                onMediaItemClicked = { id, name ->
                    navHostController.navigate(MediaDetailsRoute(id, name))
                },
                navigateToSettings = { navigateToSettings() }
            )
            mediaDetails(
                onPlayClicked = { navigateToPlayer(it) },
                onBackClicked = { navHostController.popBackStack() }
            )
            mediaCollection(
                onBackClicked = { navHostController.popBackStack() }
            )
        }
    }
}