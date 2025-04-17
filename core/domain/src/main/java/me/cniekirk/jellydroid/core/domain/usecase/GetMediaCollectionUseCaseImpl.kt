package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.model.MediaUiModel
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.domain.repository.MediaRepository
import me.cniekirk.jellydroid.core.model.CollectionKind
import javax.inject.Inject

internal class GetMediaCollectionUseCaseImpl @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
    private val mediaRepository: MediaRepository,
) : GetMediaCollectionUseCase {

    override suspend fun invoke(
        collectionId: String,
        collectionKind: CollectionKind,
        query: String?
    ): Result<List<MediaUiModel>, NetworkError> {
        return jellyfinRepository.getServerBaseUrl()
            .andThen { baseUrl ->
                mediaRepository.getMedia(collectionId, collectionKind, query)
            }
    }
}
