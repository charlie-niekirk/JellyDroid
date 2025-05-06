package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import me.cniekirk.jellydroid.core.domain.model.HomeStructure
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.model.LatestItems
import me.cniekirk.jellydroid.core.model.errors.NetworkError
import javax.inject.Inject

class GetHomeStructureUseCase @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val jellyfinRepository: JellyfinRepository
) {

    suspend operator fun invoke(): Result<HomeStructure, NetworkError> = coroutineBinding {
        val userId = appPreferencesRepository.getLoggedInUser()

        val userViews = jellyfinRepository.getUserViews().bind()
        val resumeItems = jellyfinRepository.getContinuePlayingItems(userId).bind()
        val latestMovies = jellyfinRepository.getLatestMovies(userId).bind()
        val latestShows = jellyfinRepository.getLatestShows(userId).bind()

        HomeStructure(userViews, resumeItems, LatestItems(latestMovies, latestShows))
    }
}