@file:Suppress("MatchingDeclarationName")

package me.cniekirk.jellydroid.feature.home.mobile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.navigation.routes.TopLevelRoute
import me.cniekirk.jellydroid.feature.home.R

@Serializable
data object Home : TopLevelRoute {
    override val icon = Icons.Default.Home
    override val name = R.string.home_title
}

fun EntryProviderBuilder<*>.home(
    onUserViewClicked: (String, String, CollectionKind) -> Unit,
    onResumeItemClicked: (String) -> Unit,
    onMediaItemClicked: (String, String) -> Unit,
    navigateToSettings: () -> Unit
) {
    entry<Home> {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeRoute(
            viewModel = viewModel,
            onUserViewClicked = { id, name, kind -> onUserViewClicked(id, name, kind) },
            onResumeItemClicked = { onResumeItemClicked(it) },
            onMediaItemClicked = { id, name ->
                onMediaItemClicked(id, name)
            },
            navigateToSettings = { navigateToSettings() }
        )
    }
}
