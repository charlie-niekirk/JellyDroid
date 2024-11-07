package me.cniekirk.jellydroid.feature.mediadetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

class MediaDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val jellyfinRepository: JellyfinRepository
) : ViewModel(), ContainerHost<MediaDetailsState, MediaDetailsEffect> {

    private val args = savedStateHandle.toRoute<MediaDetails>()

    override val container = container<MediaDetailsState, MediaDetailsEffect>(MediaDetailsState()) {
        loadMediaDetails(args.mediaId)
    }

    private fun loadMediaDetails(mediaId: String) = intent {

    }
}