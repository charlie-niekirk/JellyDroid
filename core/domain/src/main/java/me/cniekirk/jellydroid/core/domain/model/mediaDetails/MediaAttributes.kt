package me.cniekirk.jellydroid.core.domain.model.mediaDetails

data class MediaAttributes(
    val communityRating: CommunityRating,
    val ageRating: AgeRating?,
    val runtime: String?
)