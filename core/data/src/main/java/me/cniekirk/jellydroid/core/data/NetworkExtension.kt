package me.cniekirk.jellydroid.core.data

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import org.jellyfin.sdk.api.client.exception.ApiClientException
import org.jellyfin.sdk.api.client.exception.InvalidContentException
import org.jellyfin.sdk.api.client.exception.InvalidStatusException
import org.jellyfin.sdk.api.client.exception.MissingBaseUrlException
import org.jellyfin.sdk.api.client.exception.MissingPathVariableException
import org.jellyfin.sdk.api.client.exception.SecureConnectionException
import org.jellyfin.sdk.api.client.exception.TimeoutException
import org.jellyfin.sdk.api.sockets.exception.SocketException
import org.jellyfin.sdk.api.sockets.exception.SocketStoppedException
import timber.log.Timber

internal suspend fun <T> safeApiCall(block: suspend () -> T): Result<T, NetworkError> {
    return runCatching {
        block()
    }.mapError { throwable ->
        Timber.e(throwable)
        when (throwable) {
            is ApiClientException -> {
                when (throwable) {
                    is SocketStoppedException,
                    is SocketException,
                    is SecureConnectionException,
                    is TimeoutException -> {
                        NetworkError.ConnectionError
                    }
                    is MissingPathVariableException, is MissingBaseUrlException -> {
                        NetworkError.ClientConfigurationError
                    }
                    is InvalidStatusException, is InvalidContentException -> {
                        NetworkError.ServerError
                    }
                    else -> NetworkError.Unknown
                }
            }
            else -> NetworkError.Unknown
        }
    }
}
