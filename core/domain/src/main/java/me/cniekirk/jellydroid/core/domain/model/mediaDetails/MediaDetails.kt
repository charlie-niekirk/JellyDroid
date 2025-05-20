package me.cniekirk.jellydroid.core.domain.model.mediaDetails

import me.cniekirk.jellydroid.core.domain.model.mediaDetails.people.Person

data class MediaDetails(
    val mediaId: String,
    val synopsis: String?,
    val primaryImageUrl: String,
    val mediaAttributes: MediaAttributes,
    val mediaPath: String,
    val people: List<Person>
)