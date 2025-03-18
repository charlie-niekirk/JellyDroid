package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import org.jellyfin.sdk.model.api.BaseItemDto

interface MediaRepository {

    suspend fun getMovies(query: String? = null): Result<List<BaseItemDto>, NetworkError>

    suspend fun getTvShows(query: String? = null): Result<List<BaseItemDto>, NetworkError>
}
