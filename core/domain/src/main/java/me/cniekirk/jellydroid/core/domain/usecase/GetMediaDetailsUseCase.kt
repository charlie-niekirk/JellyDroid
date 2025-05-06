package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import me.cniekirk.jellydroid.core.domain.model.MediaDetails
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.model.errors.NetworkError
import javax.inject.Inject

class GetMediaDetailsUseCase @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val jellyfinRepository: JellyfinRepository,
) {

    suspend operator fun invoke(mediaId: String): Result<MediaDetails, NetworkError> {
        val userId = appPreferencesRepository.getLoggedInUser()

        return jellyfinRepository.getServerBaseUrl()
            .andThen { baseUrl ->
                jellyfinRepository.getMediaDetails(mediaId, userId)
            }
    }
}