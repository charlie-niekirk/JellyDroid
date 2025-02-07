package me.cniekirk.core.jellydroid.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.core.jellydroid.domain.model.MediaDetailsUiModel
import me.cniekirk.jellydroid.core.common.errors.NetworkError

interface GetMediaDetailsUseCase {

    suspend operator fun invoke(mediaId: String): Result<MediaDetailsUiModel, NetworkError>
}