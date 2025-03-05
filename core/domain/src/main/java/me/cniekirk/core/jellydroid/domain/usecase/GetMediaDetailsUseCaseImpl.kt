package me.cniekirk.core.jellydroid.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import me.cniekirk.core.jellydroid.domain.mapping.MediaDetailsMapper
import me.cniekirk.core.jellydroid.domain.model.MediaDetailsUiModel
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import javax.inject.Inject

class GetMediaDetailsUseCaseImpl @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
    private val mediaDetailsMapper: MediaDetailsMapper
) : GetMediaDetailsUseCase {

    override suspend fun invoke(mediaId: String): Result<MediaDetailsUiModel, NetworkError> {
        return jellyfinRepository.getServerBaseUrl()
            .andThen { baseUrl ->
                jellyfinRepository.getMediaDetails(mediaId)
                    .map { item -> mediaDetailsMapper.toUiModel(item, baseUrl) }
            }
    }
}
