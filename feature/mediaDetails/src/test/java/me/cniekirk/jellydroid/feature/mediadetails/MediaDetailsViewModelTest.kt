package me.cniekirk.jellydroid.feature.mediadetails

import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.cniekirk.jellydroid.core.data.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.test.SavedStateHandleRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.orbitmvi.orbit.test.test

class MediaDetailsViewModelTest {

    private val route = MediaDetails(MEDIA_ID)

    @get:Rule
    val savedStateHandleRule = SavedStateHandleRule(route)

    private val jellyfinRepository = mockk<JellyfinRepository>()

    private lateinit var sut: MediaDetailsViewModel

    @Before
    fun setup() {
        sut = MediaDetailsViewModel(
            savedStateHandleRule.savedStateHandleMock,
            jellyfinRepository
        )
    }

    @Test
    fun `test loadMediaDetails called with correct mediaId`() = runTest {
        coEvery { jellyfinRepository.getMediaDetails(MEDIA_ID) } returns Ok(MEDIA_DETAILS)

        sut.test(this) {
            expectInitialState()
            runOnCreate()

            expectState { copy(mediaId = MEDIA_DETAILS) }
        }

        coVerify(exactly = 1) { jellyfinRepository.getMediaDetails(MEDIA_ID) }
    }

    companion object {
        const val MEDIA_ID = "abcd"
        const val MEDIA_DETAILS = "details"
    }
}
