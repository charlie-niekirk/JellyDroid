package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.model.HomeStructure

interface GetHomeStructureUseCase {

    suspend operator fun invoke(): Result<HomeStructure, NetworkError>
}