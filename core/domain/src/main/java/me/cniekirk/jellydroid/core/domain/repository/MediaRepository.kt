package me.cniekirk.jellydroid.core.domain.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.MediaUiModel
import me.cniekirk.jellydroid.core.model.CollectionKind
import me.cniekirk.jellydroid.core.model.errors.NetworkError

interface MediaRepository {

    suspend fun getMedia(
        collectionId: String? = null,
        collectionKind: CollectionKind,
        query: String? = null
    ): Result<List<MediaUiModel>, NetworkError>
}
