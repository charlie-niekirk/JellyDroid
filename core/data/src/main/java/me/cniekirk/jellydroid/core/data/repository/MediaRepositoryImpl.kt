package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.safeApiCall
import me.cniekirk.jellydroid.core.datastore.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.model.CollectionKind
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.ItemSortBy
import org.jellyfin.sdk.model.api.SortOrder
import org.jellyfin.sdk.model.serializer.toUUID
import javax.inject.Inject

internal class MediaRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val appPreferencesRepository: AppPreferencesRepository
) : MediaRepository {

    override suspend fun getMedia(
        collectionId: String?,
        collectionKind: CollectionKind,
        query: String?
    ): Result<List<BaseItemDto>, NetworkError> {
        val itemTypes = when (collectionKind) {
            CollectionKind.MOVIES -> listOf(BaseItemKind.MOVIE)
            CollectionKind.SERIES -> listOf(BaseItemKind.SERIES)
        }

        return safeApiCall {
            apiClient.itemsApi.getItems(
                userId = appPreferencesRepository.getLoggedInUser().toUUID(),
                sortBy = listOf(ItemSortBy.NAME),
                sortOrder = listOf(SortOrder.ASCENDING),
                includeItemTypes = itemTypes,
                recursive = true,
                searchTerm = query,
                parentId = collectionId?.toUUID()
            ).content.items ?: listOf()
        }
    }
}
