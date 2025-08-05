package me.cniekirk.jellydroid.feature.library

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import me.cniekirk.jellydroid.core.domain.model.views.UserView
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val jellyfinRepository: JellyfinRepository
) : ViewModel(), ContainerHost<LibraryState, LibraryEffect> {

    override val container = container<LibraryState, LibraryEffect>(LibraryState()) {
        loadLibraries()
    }

    private fun loadLibraries() = intent {
        jellyfinRepository.getUserViews()
            .onSuccess { userViews ->
                reduce {
                    state.copy(
                        isLoading = false,
                        mediaLibraries = userViews
                    )
                }
            }
            .onFailure { error ->
                Timber.e(error.toString())
            }
    }

    fun onUserViewClicked(userView: UserView) = intent {
        postSideEffect(
            LibraryEffect.NavigateToLibrary(
                userView.id,
                userView.name,
                userView.collectionKind
            )
        )
    }
}
