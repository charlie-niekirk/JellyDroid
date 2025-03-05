package me.cniekirk.jellydroid.core.common.errors

sealed interface NetworkError {

    data object AuthenticationError : NetworkError

    data object ServerError : NetworkError

    data object ClientConfigurationError : NetworkError

    data object ConnectionError : NetworkError

    data object Unknown : NetworkError
}
