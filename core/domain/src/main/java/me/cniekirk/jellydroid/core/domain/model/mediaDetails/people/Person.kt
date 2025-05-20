package me.cniekirk.jellydroid.core.domain.model.mediaDetails.people

import java.util.UUID

data class Person(
    val id: UUID,
    val name: String,
    val role: String,
    val imageUrl: String,
    val creativeDomain: CreativeDomain
)