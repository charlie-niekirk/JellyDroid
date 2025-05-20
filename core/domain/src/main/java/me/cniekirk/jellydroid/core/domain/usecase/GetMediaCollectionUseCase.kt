package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import me.cniekirk.jellydroid.core.domain.model.Media
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.domain.repository.MediaRepository
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import javax.inject.Inject

class GetMediaCollectionUseCase @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
    private val mediaRepository: MediaRepository,
) {

    suspend operator fun invoke(
        collectionId: String,
        collectionKind: CollectionKind,
        query: String? = null
    ): Result<List<Media>, NetworkError> {
        return jellyfinRepository.getServerBaseUrl()
            .andThen { baseUrl ->
                mediaRepository.getMedia(collectionId, collectionKind, query)
            }
    }
}