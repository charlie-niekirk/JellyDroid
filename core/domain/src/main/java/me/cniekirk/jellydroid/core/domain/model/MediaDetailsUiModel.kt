package me.cniekirk.jellydroid.core.domain.model

sealed interface CommunityRating {

    data object NoRating : CommunityRating

    data class StarRating(val value: Float) : CommunityRating
}

data class MediaAttributes(
    val communityRating: CommunityRating,
    val ageRating: AgeRating?,
    val runtime: String?
)

data class MediaDetailsUiModel(
    val mediaId: String,
    val synopsis: String?,
    val primaryImageUrl: String,
    val mediaAttributes: MediaAttributes,
    val mediaPath: String
)
