package me.cniekirk.jellydroid.feature.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.navigation.BottomTab

fun EntryProviderBuilder<*>.library(
    navigateToUserLibrary: (String, String, CollectionKind) -> Unit
) {
    entry<Library> {
        val viewModel = hiltViewModel<LibraryViewModel>()
        LibraryRoute(
            viewModel = viewModel,
            navigateToCollection = { id, name, collectionKind ->
                navigateToUserLibrary(id, name, collectionKind)
            }
        )
    }
}

@Serializable
data object Library : NavKey, BottomTab {
    override val icon = Icons.Default.VideoLibrary
    override val name = R.string.bottom_nav_library
}
