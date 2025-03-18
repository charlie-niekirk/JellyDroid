package me.cniekirk.core.jellydroid.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.core.jellydroid.domain.model.MediaUiModel
import me.cniekirk.jellydroid.core.common.errors.NetworkError

interface GetMediaUseCase {

    suspend operator fun invoke(query: String? = null): Result<List<MediaUiModel>, NetworkError>
}
