package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.domain.model.MediaUiModel
import org.jellyfin.sdk.model.api.BaseItemDto
import javax.inject.Inject

class MediaMapper @Inject constructor() {

    fun toUiModel(dataModel: BaseItemDto, baseUrl: String?): MediaUiModel? {
        return dataModel.name?.let { name ->
            MediaUiModel(
                id = dataModel.id.toString(),
                name = name,
                thumbnailUrl = "$baseUrl/Items/${dataModel.id}/Images/Primary"
            )
        }
    }
}