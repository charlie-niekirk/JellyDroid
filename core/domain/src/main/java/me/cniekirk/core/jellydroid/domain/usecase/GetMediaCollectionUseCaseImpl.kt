package me.cniekirk.core.jellydroid.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import me.cniekirk.core.jellydroid.domain.mapping.MediaMapper
import me.cniekirk.core.jellydroid.domain.model.MediaUiModel
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.data.repository.MediaRepository
import me.cniekirk.jellydroid.core.model.CollectionKind
import javax.inject.Inject

internal class GetMediaCollectionUseCaseImpl @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
    private val mediaRepository: MediaRepository,
    private val mediaMapper: MediaMapper
) : GetMediaCollectionUseCase {

    override suspend fun invoke(
        collectionId: String,
        collectionKind: CollectionKind,
        query: String?
    ): Result<List<MediaUiModel>, NetworkError> {
        return jellyfinRepository.getServerBaseUrl()
            .andThen { baseUrl ->
                mediaRepository.getMedia(collectionId, collectionKind, query)
                    .map { itemList ->
                        itemList.mapNotNull { item ->
                            mediaMapper.toUiModel(item, baseUrl)
                        }
                    }
            }
    }
}
