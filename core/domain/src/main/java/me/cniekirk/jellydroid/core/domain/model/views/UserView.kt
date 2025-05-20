package me.cniekirk.jellydroid.core.domain.model.views

data class UserView(
    val id: String,
    val parentId: String,
    val name: String,
    val path: String,
    val imageUrl: String,
    val aspectRatio: Double,
    val collectionKind: CollectionKind
)