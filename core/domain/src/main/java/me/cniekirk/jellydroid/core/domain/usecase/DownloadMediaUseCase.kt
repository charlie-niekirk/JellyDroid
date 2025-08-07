package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import com.github.michaelbull.result.map
import kotlinx.coroutines.flow.first
import me.cniekirk.jellydroid.core.domain.model.download.DownloadParameters
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.DownloadRepository
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import javax.inject.Inject

class DownloadMediaUseCase @Inject constructor(
    private val jellyfinRepository: JellyfinRepository,
    private val downloadRepository: DownloadRepository,
    private val appPreferencesRepository: AppPreferencesRepository
) {

    suspend operator fun invoke(mediaId: String): Result<Unit, NetworkError> = coroutineBinding {
        val userId = appPreferencesRepository.getLoggedInUser().first()

        val mediaDetails = jellyfinRepository.getMediaDetails(mediaId, userId).bind()
        val baseUrl = jellyfinRepository.getServerBaseUrl().bind()
        val apiKey = jellyfinRepository.getCurrentApiKey().bind()

        val url = "$baseUrl/Items/$mediaId/Download?api_key=$apiKey"
        val imageUrl = "$baseUrl/Items/$mediaId/Images/Primary"
        val mediaName = mediaDetails.mediaName

        Ok(DownloadParameters(url, imageUrl, mediaName))
    }.map { downloadParameters ->
        if (downloadParameters.isOk) {
            val params = downloadParameters.value

            val downloadId = downloadRepository.downloadMediaFile(url = params.downloadUrl)

            appPreferencesRepository.addDownload(
                downloadId = downloadId.value.toString(),
                mediaId = mediaId,
                mediaName = params.mediaName,
                mediaThumbnailUrl = params.imageUrl
            )
        } else {
            Err(NetworkError.Unknown)
        }
    }
}