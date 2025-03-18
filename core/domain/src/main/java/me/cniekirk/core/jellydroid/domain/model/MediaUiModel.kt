package me.cniekirk.core.jellydroid.domain.model

import org.jellyfin.sdk.model.UUID

data class MediaUiModel(
    val id: UUID,
    val name: String,
    val thumbnailUrl: String,
)
