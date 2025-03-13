package me.cniekirk.jellydroid.feature.mediadetails

import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.cniekirk.core.jellydroid.domain.model.AgeRating
import me.cniekirk.core.jellydroid.domain.model.CommunityRating
import me.cniekirk.core.jellydroid.domain.model.MediaAttributes
import me.cniekirk.core.jellydroid.domain.model.MediaDetailsUiModel
import me.cniekirk.core.jellydroid.domain.usecase.GetMediaDetailsUseCase
import me.cniekirk.jellydroid.core.test.SavedStateHandleRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.orbitmvi.orbit.test.test

class MediaDetailsViewModelTest {

    private val route = MediaDetails(MEDIA_ID, MEDIA_TITLE)

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(route)

    private val getMediaDetailsUseCase = mockk<GetMediaDetailsUseCase>()

    private lateinit var underTest: MediaDetailsViewModel

    @Before
    fun setup() {
        underTest = MediaDetailsViewModel(
            savedStateHandleRule.savedStateHandleMock,
            getMediaDetailsUseCase
        )
    }

    @Test
    fun `test loadMediaDetails called with correct mediaId`() = runTest {
        val expected = MediaDetailsUiModel(
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
            mediaPath = "path/to/media"
        )
        coEvery { getMediaDetailsUseCase(MEDIA_ID) } returns Ok(expected)

        underTest.test(this) {
            expectInitialState()
            runOnCreate()

            expectState {
                copy(
                    isLoading = false,
                    mediaDetailsUiModel = expected
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
