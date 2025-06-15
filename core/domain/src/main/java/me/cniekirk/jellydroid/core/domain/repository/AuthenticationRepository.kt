package me.cniekirk.jellydroid.core.domain.repository

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.servers.User

interface AuthenticationRepository {

    suspend fun connectToServer(address: String): Result<String, NetworkError>

    suspend fun authenticateUser(username: String, password: String): Result<User, NetworkError>
}
