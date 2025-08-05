package me.cniekirk.jellydroid.core.domain.usecase

import com.github.michaelbull.result.Result
import me.cniekirk.jellydroid.core.domain.model.FavoriteStatus
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import javax.inject.Inject

class SetItemFavoriteStatusUseCase @Inject constructor(
    private val jellyfinRepository: JellyfinRepository
) {

    suspend operator fun invoke(
        itemId: String,
        favoriteStatus: FavoriteStatus
    ): Result<Unit, NetworkError> =
        jellyfinRepository.setFavoriteStatus(itemId, favoriteStatus)
}