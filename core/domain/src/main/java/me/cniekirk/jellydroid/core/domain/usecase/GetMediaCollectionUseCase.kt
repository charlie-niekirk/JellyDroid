package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import kotlinx.coroutines.flow.first
import me.cniekirk.jellydroid.core.domain.model.Media
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.domain.repository.MediaRepository
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import javax.inject.Inject

class GetMediaCollectionUseCase @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
    private val mediaRepository: MediaRepository,
    private val appPreferencesRepository: AppPreferencesRepository
) {

    suspend operator fun invoke(
        collectionId: String,
        collectionKind: CollectionKind,
        query: String? = null
    ): Result<List<Media>, NetworkError> {
        return jellyfinRepository.getServerBaseUrl()
            .andThen { baseUrl ->
                val userId = appPreferencesRepository.getLoggedInUser().first()
                mediaRepository.getMedia(userId, collectionKind, collectionId, query)
            }
    }
}