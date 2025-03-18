package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.safeApiCall
import me.cniekirk.jellydroid.core.datastore.repository.AppPreferencesRepository
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.MediaType
import org.jellyfin.sdk.model.serializer.toUUID
import javax.inject.Inject

internal class MediaRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val appPreferencesRepository: AppPreferencesRepository
) : MediaRepository {

    override suspend fun getMovies(query: String?): Result<List<BaseItemDto>, NetworkError> {
        return safeApiCall {
            apiClient.itemsApi.getItems(
                userId = appPreferencesRepository.getLoggedInUser().toUUID(),
                mediaTypes = listOf(MediaType.VIDEO),
                includeItemTypes = listOf(BaseItemKind.MOVIE),
                searchTerm = query
            ).content.items ?: listOf()
        }
    }

    override suspend fun getTvShows(query: String?): Result<List<BaseItemDto>, NetworkError> {
        return safeApiCall {
            apiClient.itemsApi.getItems(
                userId = appPreferencesRepository.getLoggedInUser().toUUID(),
                mediaTypes = listOf(MediaType.VIDEO),
                includeItemTypes = listOf(BaseItemKind.SERIES),
                searchTerm = query
            ).content.items ?: listOf()
        }
    }
}
