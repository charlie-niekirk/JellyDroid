package me.cniekirk.jellydroid.core.domain.model.latest

data class LatestItems(
    val movies: List<LatestItem>,
    val shows: List<LatestItem>
)