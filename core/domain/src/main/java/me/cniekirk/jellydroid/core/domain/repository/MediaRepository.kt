package me.cniekirk.jellydroid.core.domain.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.Media
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind

interface MediaRepository {

    suspend fun getMedia(
        collectionId: String? = null,
        collectionKind: CollectionKind,
        query: String? = null
    ): Result<List<Media>, NetworkError>
}
