package me.cniekirk.core.jellydroid.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.core.jellydroid.domain.model.MediaUiModel
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.model.CollectionKind

interface GetMediaCollectionUseCase {

    suspend operator fun invoke(
        collectionId: String,
        collectionKind: CollectionKind,
        query: String? = null
    ): Result<List<MediaUiModel>, NetworkError>
}
