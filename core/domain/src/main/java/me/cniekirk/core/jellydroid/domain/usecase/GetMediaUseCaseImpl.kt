package me.cniekirk.core.jellydroid.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import me.cniekirk.core.jellydroid.domain.mapping.MediaMapper
import me.cniekirk.core.jellydroid.domain.model.MediaUiModel
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.data.repository.MediaRepository
import javax.inject.Inject

internal class GetMediaUseCaseImpl @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
    private val mediaRepository: MediaRepository,
    private val mediaMapper: MediaMapper
) : GetMediaUseCase {

    override suspend fun invoke(query: String?): Result<List<MediaUiModel>, NetworkError> {
        return jellyfinRepository.getServerBaseUrl()
            .andThen { baseUrl ->
                mediaRepository.getMovies(query)
                    .map { itemList ->
                        itemList.mapNotNull { item ->
                            mediaMapper.toUiModel(item, baseUrl)
                        }
                    }
            }
    }
}
