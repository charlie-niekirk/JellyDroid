package me.cniekirk.jellydroid.feature.mediacollection

import androidx.annotation.Keep
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.enterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popExitAnimation
import me.cniekirk.jellydroid.core.model.CollectionKind

fun NavGraphBuilder.mediaCollection() {
    composable<MediaCollection>(
        enterTransition = { enterAnimation() },
        popExitTransition = { popExitAnimation() }
    ) {
        val viewModel = hiltViewModel<MediaCollectionViewModel>()
        MediaCollectionRoute(viewModel)
    }
}

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
)
