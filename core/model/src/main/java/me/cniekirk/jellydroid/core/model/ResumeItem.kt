package me.cniekirk.jellydroid.core.model

data class ResumeItem(
    val id: String,
    val name: String,
    val imageUrl: String,
    val aspectRatio: Double,
    val playedPercentage: Float
)