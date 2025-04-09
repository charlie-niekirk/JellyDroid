package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.model.HomeStructure
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.model.LatestItems
import javax.inject.Inject

class GetHomeStructureUseCase @Inject constructor(
    private val jellyfinRepository: JellyfinRepository
) {
    suspend operator fun invoke(): Result<HomeStructure, NetworkError> = coroutineBinding {
        val userViews = jellyfinRepository.getUserViews().bind()
        val resumeItems = jellyfinRepository.getContinuePlayingItems().bind()
        val latestMovies = jellyfinRepository.getLatestMovies().bind()
        val latestShows = jellyfinRepository.getLatestShows().bind()

        HomeStructure(userViews, resumeItems, LatestItems(latestMovies, latestShows))
    }
}
