package me.cniekirk.jellydroid.feature.mediacollection

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import me.cniekirk.jellydroid.core.designsystem.theme.enterAnimation
import me.cniekirk.jellydroid.core.designsystem.theme.popExitAnimation

@Serializable
data class MediaCollection(
    val collectionName: String,
    val collectionId: String
)

fun NavGraphBuilder.mediaCollection() {
    composable<MediaCollection>(
        enterTransition = { enterAnimation() },
        popExitTransition = { popExitAnimation() }
    ) {
        val viewModel = hiltViewModel<MediaCollectionViewModel>()
        MediaCollectionRoute(viewModel)
    }
}