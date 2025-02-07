package me.cniekirk.jellydroid.feature.home

import androidx.lifecycle.ViewModel
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import me.cniekirk.core.jellydroid.domain.usecase.GetHomeStructureUseCase
import me.cniekirk.jellydroid.feature.home.mobile.HomeEffect
import me.cniekirk.jellydroid.feature.home.mobile.HomeState
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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

    fun queryChanged(query: String) = blockingIntent {
        reduce {
            state.copy(searchQuery = query)
        }
    }
}