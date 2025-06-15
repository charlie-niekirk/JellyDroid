package me.cniekirk.jellydroid.feature.mediacollection

import androidx.annotation.Keep
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import kotlinx.serialization.Serializable

@Keep
@Serializable
enum class CollectionType {
    MOVIES,
    SERIES
}

@Serializable
data class MediaCollection(
    val collectionId: String,
    val collectionName: String,
    val collectionType: CollectionType
) : NavKey

fun EntryProviderBuilder<*>.mediaCollection(
    onBackClicked: () -> Unit
) {
    entry<MediaCollection> { key ->
        val viewModel = hiltViewModel<MediaCollectionViewModel, MediaCollectionViewModel.Factory>(
            creationCallback = { factory -> factory.create(key) }
        )

        MediaCollectionRoute(
            viewModel = viewModel,
            onBackClicked = { onBackClicked() }
        )
    }
}
