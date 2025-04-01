package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.model.CollectionKind
import org.jellyfin.sdk.model.api.BaseItemDto

interface MediaRepository {

    suspend fun getMedia(
        collectionId: String? = null,
        collectionKind: CollectionKind,
        query: String? = null
    ): Result<List<BaseItemDto>, NetworkError>
}
