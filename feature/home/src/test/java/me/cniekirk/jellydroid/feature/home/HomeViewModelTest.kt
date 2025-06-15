package me.cniekirk.jellydroid.feature.home

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.latest.LatestItems
import me.cniekirk.jellydroid.core.domain.model.views.HomeStructure
import me.cniekirk.jellydroid.core.domain.usecase.GetHomeStructureUseCase
import me.cniekirk.jellydroid.feature.home.mobile.HomeEffect
import me.cniekirk.jellydroid.feature.home.mobile.HomeViewModel
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test.test

class HomeViewModelTest {

    private lateinit var underTest: HomeViewModel

    private val getHomeStructureUseCase = mockk<GetHomeStructureUseCase>()

    @Before
    fun setup() {
        underTest = HomeViewModel(getHomeStructureUseCase)
    }

    @Test
    fun `when initialised and getHomeStructureUseCase returns success then verify state`() = runTest {
        // Given
        coEvery { getHomeStructureUseCase.invoke() } returns Ok(expectedHomeStructure)

        // When
        underTest.test(this) {
            expectInitialState()
            runOnCreate()

            // Then
            expectState {
                copy(
                    isLoading = false,
                    userProfileImage = expectedHomeStructure.profileImageUrl,
                    userViews = expectedHomeStructure.userViews.toImmutableList(),
                    resumeItems = expectedHomeStructure.resumeItems.toImmutableList(),
                    latestMovies = expectedHomeStructure.latestItems.movies.toImmutableList(),
                    latestShows = expectedHomeStructure.latestItems.shows.toImmutableList()
                )
            }
        }

        coVerify(exactly = 1) { getHomeStructureUseCase.invoke() }
    }

    @Test
    fun `when initialised and getHomeStructureUseCase returns error then verify ShowError effect`() = runTest {
        // Given
        coEvery { getHomeStructureUseCase.invoke() } returns Err(expectedError)

        // When
        underTest.test(this) {
            expectInitialState()
            runOnCreate()

            // Then
            expectSideEffect(HomeEffect.ShowError(expectedError))
        }

        coVerify(exactly = 1) { getHomeStructureUseCase.invoke() }
    }

    companion object {
        val expectedHomeStructure = HomeStructure(
            profileImageUrl = "",
            userViews = listOf(),
            resumeItems = listOf(),
            latestItems = LatestItems(
                movies = listOf(),
                shows = listOf()
            )
        )

        val expectedError = NetworkError.Unknown
    }
}
