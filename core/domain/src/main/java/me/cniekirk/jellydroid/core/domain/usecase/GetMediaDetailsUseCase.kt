package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.model.MediaDetailsUiModel

interface GetMediaDetailsUseCase {

    suspend operator fun invoke(mediaId: String): Result<MediaDetailsUiModel, NetworkError>
}
