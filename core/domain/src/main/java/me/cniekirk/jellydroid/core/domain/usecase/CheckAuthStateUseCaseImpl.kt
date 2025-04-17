package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.fold
import me.cniekirk.jellydroid.core.domain.model.error.CheckAuthStateError
import me.cniekirk.jellydroid.core.domain.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import javax.inject.Inject

internal class CheckAuthStateUseCaseImpl @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val jellyfinRepository: JellyfinRepository
) : CheckAuthStateUseCase {

    override suspend fun invoke(): Result<String, CheckAuthStateError> {
        val currentServerId = appPreferencesRepository.getCurrentServer()
        if (currentServerId.isEmpty()) return Err(CheckAuthStateError.NoPreviousAuth)

        val serverResult = jellyfinRepository.getServerWithUsersById(currentServerId)

        return serverResult.fold(
            success = { serverWithUsers ->
                val defaultUser = serverWithUsers.users.firstOrNull()

                if (defaultUser != null) {
                    jellyfinRepository.updateClient(
                        accessToken = defaultUser.accessToken,
                        baseUrl = serverWithUsers.server.baseUrl
                    )

                    // Get user based on auth token
                    jellyfinRepository.getUserFromToken().fold(
                        success = { userId ->
                            Ok(userId)
                        },
                        failure = {
                            Err(CheckAuthStateError.AuthTokenOutdated)
                        }
                    )
                } else {
                    Err(CheckAuthStateError.NoPreviousAuth)
                }
            },
            failure = { err ->
                Err(CheckAuthStateError.NoPreviousAuth)
            }
        )
    }
}