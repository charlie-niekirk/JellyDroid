package me.cniekirk.core.jellydroid.domain.mapping

import me.cniekirk.core.jellydroid.domain.model.MediaUiModel
import org.jellyfin.sdk.model.api.BaseItemDto
import javax.inject.Inject

class MediaMapper @Inject constructor() {

    fun toUiModel(dataModel: BaseItemDto, baseUrl: String): MediaUiModel? {
        return dataModel.name?.let { name ->
            MediaUiModel(
                id = dataModel.id,
                name = name,
                thumbnailUrl = "$baseUrl/Items/${dataModel.id}/Images/Primary"
            )
        }
    }
}
