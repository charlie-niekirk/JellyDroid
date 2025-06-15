package me.cniekirk.jellydroid.navigation

import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultExit
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopEnter
import me.cniekirk.jellydroid.core.designsystem.theme.activityDefaultPopExit
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.navigation.TopLevelBackStack
import me.cniekirk.jellydroid.feature.home.mobile.Home
import me.cniekirk.jellydroid.feature.home.mobile.home
import me.cniekirk.jellydroid.feature.mediacollection.CollectionType
import me.cniekirk.jellydroid.feature.mediacollection.MediaCollection
import me.cniekirk.jellydroid.feature.mediacollection.mediaCollection
import me.cniekirk.jellydroid.feature.mediadetails.MediaDetails
import me.cniekirk.jellydroid.feature.mediadetails.mediaDetails
import me.cniekirk.jellydroid.feature.mediaplayer.MediaPlayer
import me.cniekirk.jellydroid.feature.mediaplayer.mediaPlayer
import me.cniekirk.jellydroid.feature.onboarding.OnboardingNavigation
import me.cniekirk.jellydroid.feature.onboarding.onboardingModuleEntries
import me.cniekirk.jellydroid.feature.settings.Settings
import me.cniekirk.jellydroid.feature.settings.settings

@Serializable
data object RootHome : NavKey

@Composable
fun JellydroidRootNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(OnboardingNavigation.Landing)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
//            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            activityDefaultEnter() togetherWith activityDefaultExit()
        },
        popTransitionSpec = {
            activityDefaultPopEnter() togetherWith activityDefaultPopExit()
        },
        entryProvider = entryProvider {
            onboardingModuleEntries(
                navBackStack = backStack,
                navigateToHome = {
                    backStack.add(RootHome)
                    backStack.removeAll(backStack.filterIsInstance<OnboardingNavigation.OnboardingNavKey>())
                }
            )

            entry<RootHome> {
                JellydroidTabsNavHost(
                    modifier = Modifier.fillMaxSize(),
                    navigateToSettings = { backStack.add(Settings) },
                    navigateToPlayer = { backStack.add(MediaPlayer(it)) }
                )
            }

            settings(onBackPressed = { backStack.removeLastOrNull() })

            mediaPlayer()
        }
    )
}

@Composable
fun JellydroidTabsNavHost(
    modifier: Modifier,
    navigateToSettings: () -> Unit,
    navigateToPlayer: (String) -> Unit
) {
    val navSuiteType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
        currentWindowAdaptiveInfo()
    )

    val appBackstack = remember {
        TopLevelBackStack<Any>(startKey = Home)
    }

    val routes = listOf(
        Home
    )

    NavigationSuiteScaffoldLayout(
        navigationSuite = {
            if (navSuiteType == NavigationSuiteType.NavigationRail) {
                NavigationRail {
                    Spacer(Modifier.weight(1f))
                    routes.forEach { topLevelRoute ->
                        val isSelected = topLevelRoute == appBackstack.topLevelKey

                        NavigationRailItem(
                            icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.icon.name) },
                            label = { Text(stringResource(topLevelRoute.name)) },
                            selected = isSelected,
                            onClick = {
                                appBackstack.addTopLevel(topLevelRoute)
                            }
                        )

                        Spacer(Modifier.height(8.dp))
                    }
                    Spacer(Modifier.weight(1f))
                }
            } else {
                NavigationSuite {
                    routes.forEach { topLevelRoute ->
                        val isSelected = topLevelRoute == appBackstack.topLevelKey

                        item(
                            icon = { Icon(topLevelRoute.icon, contentDescription = topLevelRoute.icon.name) },
                            label = { Text(stringResource(topLevelRoute.name)) },
                            selected = isSelected,
                            onClick = {
                                appBackstack.addTopLevel(topLevelRoute)
                            }
                        )
                    }
                }
            }
        }
    ) {
        NavDisplay(
            backStack = appBackstack.backStack,
            onBack = { appBackstack.removeLast() },
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                home(
                    onUserViewClicked = { id, name, kind ->
                        val type = when (kind) {
                            CollectionKind.MOVIES -> CollectionType.MOVIES
                            CollectionKind.SERIES -> CollectionType.SERIES
                        }
                        appBackstack.add(MediaCollection(id, name, type))
                    },
                    onResumeItemClicked = {},
                    onMediaItemClicked = { id, name ->
                        appBackstack.add(MediaDetails(id, name))
                    },
                    navigateToSettings = { navigateToSettings() }
                )

                mediaCollection(
                    onBackClicked = { appBackstack.removeLast() }
                )

                mediaDetails(
                    onPlayClicked = { navigateToPlayer(it) },
                    onBackClicked = { appBackstack.removeLast() }
                )
            },
        )
    }
}