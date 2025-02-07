package me.cniekirk.core.jellydroid.domain.mapping

import me.cniekirk.core.jellydroid.domain.model.AgeRating
import me.cniekirk.core.jellydroid.domain.model.MediaDetailsUiModel
import org.jellyfin.sdk.model.api.BaseItemDto

class MediaDetailsMapper : DomainMapper<BaseItemDto, MediaDetailsUiModel> {

    override fun toUiModel(dataModel: BaseItemDto): MediaDetailsUiModel {
        return MediaDetailsUiModel(
            name = dataModel.name ?: "",
            synopsis = dataModel.overview ?: "",
            primaryImageUrl = /*dataModel.image*/"",
            ageRating = AgeRating(
                dataModel.officialRating ?: "",
                ratingImageUrl = ""
            )
        )
    }
}