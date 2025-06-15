package me.cniekirk.jellydroid.feature.mediacollection

import androidx.annotation.Keep
import androidx.compose.animation.togetherWith
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.enterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.exitAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popEnterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popExitAnimation

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
    entry<MediaCollection>(
        metadata = NavDisplay.transitionSpec {
            enterAnimation() togetherWith exitAnimation()
        } + NavDisplay.popTransitionSpec {
            popEnterAnimation() togetherWith popExitAnimation()
        } + NavDisplay.predictivePopTransitionSpec {
            popEnterAnimation() togetherWith popExitAnimation()
        }
    ) { key ->
        val viewModel = hiltViewModel<MediaCollectionViewModel, MediaCollectionViewModel.Factory>(
            creationCallback = { factory -> factory.create(key) }
        )

        MediaCollectionRoute(
            viewModel = viewModel,
            onBackClicked = { onBackClicked() }
        )
    }
}
