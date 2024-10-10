package me.cniekirk.core.jellydroid.domain.usecase

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.coroutineBinding
import me.cniekirk.core.jellydroid.domain.model.HomeStructure
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import javax.inject.Inject

class GetHomeStructureUseCase @Inject constructor(
    private val jellyfinRepository: JellyfinRepository
) {
    suspend operator fun invoke(): Result<HomeStructure, NetworkError> = coroutineBinding {
        val userViews = jellyfinRepository.getUserViews().bind()
        val resumeItems = jellyfinRepository.getContinuePlayingItems().bind()
        HomeStructure(userViews, resumeItems)
    }
}