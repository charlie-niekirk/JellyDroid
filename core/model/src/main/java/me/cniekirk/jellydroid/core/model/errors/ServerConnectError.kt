package me.cniekirk.jellydroid.core.model.errors

sealed interface ServerConnectError {

    data object FetchDetailsError : ServerConnectError

    data object InitialConnectError : ServerConnectError

    data class NetworkingError(val error: NetworkError) : ServerConnectError
}
