package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.error.CheckAuthStateError

interface CheckAuthStateUseCase {

    suspend operator fun invoke(): Result<String, CheckAuthStateError>
}