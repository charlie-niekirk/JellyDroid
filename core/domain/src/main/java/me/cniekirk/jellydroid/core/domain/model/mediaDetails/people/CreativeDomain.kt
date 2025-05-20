package me.cniekirk.jellydroid.core.domain.model.mediaDetails.people

sealed class CreativeDomain(val description: String) {
    /** Represents an unknown or unspecified creative role. */
    data object Unspecified : CreativeDomain("Unspecified Role")

    /** Represents roles primarily related to film and television production. */
    data class FilmAndTelevision(val subRole: FilmTvSubRole) : CreativeDomain("Film & Television")

    /** Represents roles primarily related to music creation and production. */
    data class Music(val subRole: MusicSubRole) : CreativeDomain("Music")

    /** Represents roles primarily related to literary arts, comics, and publishing. */
    data class PublishingAndLiteraryArts(val subRole: PublishingSubRole) : CreativeDomain("Publishing & Literary Arts")
}
