package me.cniekirk.jellydroid.core.domain.model.mediaDetails

sealed interface CommunityRating {

    data object NoRating : CommunityRating

    data class StarRating(val value: Float) : CommunityRating
}