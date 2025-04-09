package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.model.MediaUiModel
import me.cniekirk.jellydroid.core.model.CollectionKind

interface GetMediaCollectionUseCase {

    suspend operator fun invoke(
        collectionId: String,
        collectionKind: CollectionKind,
        query: String? = null
    ): Result<List<MediaUiModel>, NetworkError>
}
