package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import kotlinx.coroutines.flow.first
import me.cniekirk.jellydroid.core.domain.model.views.HomeStructure
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.latest.LatestItems
import javax.inject.Inject

class GetHomeStructureUseCase @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val jellyfinRepository: JellyfinRepository,
) {

    suspend operator fun invoke(): Result<HomeStructure, NetworkError> = coroutineBinding {
        val currentServer = appPreferencesRepository.getCurrentServer().first()
        val userId = appPreferencesRepository.getLoggedInUser().first()

        val user = jellyfinRepository.getUserById(currentServer, userId).bind()
        val userViews = jellyfinRepository.getUserViews().bind()
        val resumeItems = jellyfinRepository.getContinuePlayingItems(userId).bind()
        val latestMovies = jellyfinRepository.getLatestMovies(userId).bind()
        val latestShows = jellyfinRepository.getLatestShows(userId).bind()

        HomeStructure(user.profileImageUrl, userViews, resumeItems, LatestItems(latestMovies, latestShows))
    }
}