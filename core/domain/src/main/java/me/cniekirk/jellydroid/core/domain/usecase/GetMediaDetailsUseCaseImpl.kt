package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.model.MediaDetails
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import javax.inject.Inject

internal class GetMediaDetailsUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val jellyfinRepository: JellyfinRepository,
) : GetMediaDetailsUseCase {

    override suspend fun invoke(mediaId: String): Result<MediaDetails, NetworkError> {
        val userId = appPreferencesRepository.getLoggedInUser()

        return jellyfinRepository.getServerBaseUrl()
            .andThen { baseUrl ->
                jellyfinRepository.getMediaDetails(mediaId, userId)
            }
    }
}
