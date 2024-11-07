package me.cniekirk.jellydroid.core.model

data class MediaDetails(
    val id: String,
    val name: String,
    val container: String,
    val premierDate: String,
    val criticRating: Int,
    val communityRating: Double,
    val productionLocations: List<String>,
    val ageRating: String,
    val synopsis: String,
    val genres: List<String>,
    val people: List<Person>
)
