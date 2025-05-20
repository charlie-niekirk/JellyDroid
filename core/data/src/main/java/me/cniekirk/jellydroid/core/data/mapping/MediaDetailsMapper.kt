package me.cniekirk.jellydroid.core.data.mapping

import me.cniekirk.jellydroid.core.domain.model.mediaDetails.AgeRating
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.CommunityRating
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaAttributes
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaDetails
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.people.CreativeDomain
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.people.FilmTvSubRole
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.people.MusicSubRole
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.people.Person
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.people.PublishingSubRole
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemPerson
import org.jellyfin.sdk.model.api.PersonKind
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MediaDetailsMapper @Inject constructor() {

    fun toMediaDetails(dataModel: BaseItemDto, baseUrl: String?): MediaDetails {
        val rating = dataModel.communityRating?.let {
            CommunityRating.StarRating(it)
        } ?: CommunityRating.NoRating

        return MediaDetails(
            mediaId = dataModel.id.toString(),
            synopsis = dataModel.overview,
            primaryImageUrl = "$baseUrl/Items/${dataModel.id}/Images/Backdrop",
            mediaAttributes = MediaAttributes(
                communityRating = rating,
                ageRating = dataModel.officialRating?.let {
                    AgeRating(
                        ratingName = it,
                        ratingImageUrl = ""
                    )
                },
                runtime = dataModel.runTimeTicks?.toRuntime()
            ),
            mediaPath = baseUrl + dataModel.path,
            people = dataModel.people?.mapNotNull { it.toPerson(baseUrl.toString()) } ?: emptyList()
        )
    }

    private fun Long.toRuntime(): String {
        val duration = this.toDuration(DurationUnit.MICROSECONDS)
        val components = duration.toComponents { hours, minutes, seconds, _ ->
            Triple(hours, minutes, seconds)
        }

        return buildString {
            components.run {
                val correctH = if (second > 0) "h " else "h"
                val correctM = if (first == 0L && third > 0) "m " else "m"

                if (first > 0) append(first).append(correctH)
                if (second > 0) append(second).append(correctM)
                if (first == 0L && third > 0) append(third).append("s")
            }
        }
    }

    private fun BaseItemPerson.toPerson(baseUrl: String): Person? {
        val name = name ?: return null
        val role = role ?: return null

        val imageUrl = "$baseUrl/Items/$id/Images/Primary"
        val creativeDomain = mapPersonKindToCreativeDomain(type)

        return Person(
            id = id,
            name = name,
            role = role,
            imageUrl = imageUrl,
            creativeDomain = creativeDomain
        )
    }

    private fun mapPersonKindToCreativeDomain(personKind: PersonKind): CreativeDomain {
        return when (personKind) {
            PersonKind.UNKNOWN -> CreativeDomain.Unspecified

            PersonKind.ACTOR -> CreativeDomain.FilmAndTelevision(FilmTvSubRole.ACTOR)
            PersonKind.DIRECTOR -> CreativeDomain.FilmAndTelevision(FilmTvSubRole.DIRECTOR)
            PersonKind.GUEST_STAR -> CreativeDomain.FilmAndTelevision(FilmTvSubRole.GUEST_STAR)
            PersonKind.PRODUCER -> CreativeDomain.FilmAndTelevision(FilmTvSubRole.PRODUCER)
            PersonKind.CREATOR -> CreativeDomain.FilmAndTelevision(FilmTvSubRole.CREATOR)
            PersonKind.WRITER -> CreativeDomain.FilmAndTelevision(FilmTvSubRole.WRITER)


            PersonKind.COMPOSER -> CreativeDomain.Music(MusicSubRole.COMPOSER)
            PersonKind.CONDUCTOR -> CreativeDomain.Music(MusicSubRole.CONDUCTOR)
            PersonKind.LYRICIST -> CreativeDomain.Music(MusicSubRole.LYRICIST)
            PersonKind.ARRANGER -> CreativeDomain.Music(MusicSubRole.ARRANGER)
            PersonKind.ENGINEER -> CreativeDomain.Music(MusicSubRole.ENGINEER)
            PersonKind.MIXER -> CreativeDomain.Music(MusicSubRole.MIXER)
            PersonKind.REMIXER -> CreativeDomain.Music(MusicSubRole.REMIXER)
            PersonKind.ARTIST -> CreativeDomain.Music(MusicSubRole.ARTIST)
            PersonKind.ALBUM_ARTIST -> CreativeDomain.Music(MusicSubRole.ALBUM_ARTIST)

            PersonKind.AUTHOR -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.AUTHOR)
            PersonKind.ILLUSTRATOR -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.ILLUSTRATOR)
            PersonKind.PENCILLER -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.PENCILLER)
            PersonKind.INKER -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.INKER)
            PersonKind.COLORIST -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.COLORIST)
            PersonKind.LETTERER -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.LETTERER)
            PersonKind.COVER_ARTIST -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.COVER_ARTIST)
            PersonKind.EDITOR -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.EDITOR)
            PersonKind.TRANSLATOR -> CreativeDomain.PublishingAndLiteraryArts(PublishingSubRole.TRANSLATOR)
        }
    }
}