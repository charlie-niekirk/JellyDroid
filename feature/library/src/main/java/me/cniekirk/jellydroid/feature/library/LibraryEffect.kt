package me.cniekirk.jellydroid.feature.library

import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind

sealed interface LibraryEffect {

    data class NavigateToLibrary(
        val collectionId: String,
        val collectionName: String,
        val collectionKind: CollectionKind
    ) : LibraryEffect
}
