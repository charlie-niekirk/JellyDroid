package me.cniekirk.core.jellydroid.domain.usecase

import com.github.michaelbull.result.Result
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

    override suspend fun invoke(mediaId: String): Result<MediaDetailsUiModel, NetworkError> =
        jellyfinRepository.getMediaDetails(mediaId)
            .map(mediaDetailsMapper::toUiModel)
}