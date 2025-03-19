package me.cniekirk.jellydroid.core.model

enum class CollectionKind {
    MOVIES,
    SERIES
}

data class UserView(
    val id: String,
    val parentId: String,
    val name: String,
    val path: String,
    val imageUrl: String,
    val aspectRatio: Double,
    val collectionKind: CollectionKind
)
