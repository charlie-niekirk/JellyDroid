package me.cniekirk.core.jellydroid.domain.model

data class MediaDetailsUiModel(
    val name: String,
    val synopsis: String,
    val primaryImageUrl: String,
    val ageRating: AgeRating,
)
