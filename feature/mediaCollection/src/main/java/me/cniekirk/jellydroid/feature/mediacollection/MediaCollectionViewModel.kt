package me.cniekirk.jellydroid.feature.mediacollection

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MediaCollectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<MediaCollectionState, MediaCollectionEffect> {

    private val args = savedStateHandle.toRoute<MediaCollection>()

    override val container = container<MediaCollectionState, MediaCollectionEffect>(MediaCollectionState(args.collectionName)) {
        // TODO: Load media collection
    }


}