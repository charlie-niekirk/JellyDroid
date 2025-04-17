package me.cniekirk.jellydroid.feature.home.mobile

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.jellydroid.core.domain.usecase.GetHomeStructureUseCase
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getHomeStructureUseCase: GetHomeStructureUseCase
) : ViewModel(), ContainerHost<HomeState, HomeEffect> {

    override val container = container<HomeState, HomeEffect>(HomeState()) {
        loadUserViews()
    }

    private fun loadUserViews() = intent {
        getHomeStructureUseCase()
            .onSuccess { homeStructure ->
                reduce {
                    state.copy(
                        isLoading = false,
                        userViews = homeStructure.userViews.toImmutableList(),
                        resumeItems = homeStructure.resumeItems.toImmutableList(),
                        latestMovies = homeStructure.latestItems.movies.toImmutableList(),
                        latestShows = homeStructure.latestItems.shows.toImmutableList()
                    )
                }
            }
            .onFailure { error ->
                Timber.e("Error: $error")
                postSideEffect(HomeEffect.ShowError(error))
            }
    }
}
