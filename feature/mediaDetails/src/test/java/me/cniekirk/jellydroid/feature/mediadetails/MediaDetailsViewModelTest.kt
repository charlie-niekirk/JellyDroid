package me.cniekirk.jellydroid.feature.mediadetails

import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.AgeRating
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.CommunityRating
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaAttributes
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaDetailsUseCase
import me.cniekirk.jellydroid.core.domain.usecase.SetItemFavoriteStatusUseCase
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test.test

class MediaDetailsViewModelTest {

    private val getMediaDetailsUseCase = mockk<GetMediaDetailsUseCase>()
    private val setItemFavoriteStatusUseCase = mockk<SetItemFavoriteStatusUseCase>()

    private lateinit var underTest: MediaDetailsViewModel

    @Before
    fun setup() {
        underTest = MediaDetailsViewModel(
            args = MediaDetails(MEDIA_ID, MEDIA_TITLE),
            getMediaDetailsUseCase = getMediaDetailsUseCase,
            setItemFavoriteStatusUseCase = setItemFavoriteStatusUseCase
        )
    }

    @Test
    fun `test loadMediaDetails called with correct mediaId`() = runTest {
        val expected = me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaDetails(
            mediaId = MEDIA_ID,
            synopsis = "synopsis",
            primaryImageUrl = "https://image.com/img.png",
            mediaAttributes = MediaAttributes(
                communityRating = CommunityRating.NoRating,
                ageRating = AgeRating(
                    ratingName = "12A",
                    ratingImageUrl = null
                ),
                runtime = null
            ),
            mediaPath = "path/to/media",
            people = listOf(),
            mediaName = "",
            isFavorite = false,
            trailers = emptyList()
        )
        coEvery { getMediaDetailsUseCase(MEDIA_ID) } returns Ok(expected)

        underTest.test(this) {
            runOnCreate()

            expectState {
                copy(
                    isLoading = false,
                    mediaDetails = expected
                )
            }
        }

        coVerify(exactly = 1) { getMediaDetailsUseCase(MEDIA_ID) }
    }

    companion object {
        const val MEDIA_ID = "abcd"
        const val MEDIA_TITLE = "title"
    }
}
