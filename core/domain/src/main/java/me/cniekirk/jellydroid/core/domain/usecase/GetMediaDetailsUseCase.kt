package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.model.MediaDetails

interface GetMediaDetailsUseCase {

    suspend operator fun invoke(mediaId: String): Result<MediaDetails, NetworkError>
}
