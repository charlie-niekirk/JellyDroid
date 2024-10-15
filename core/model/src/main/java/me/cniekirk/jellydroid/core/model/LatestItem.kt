package me.cniekirk.jellydroid.core.model

data class LatestItem(
    val id: String,
    val name: String,
    val imageUrl: String,
)

data class LatestItems(
    val movies: List<LatestItem>,
    val shows: List<LatestItem>
)