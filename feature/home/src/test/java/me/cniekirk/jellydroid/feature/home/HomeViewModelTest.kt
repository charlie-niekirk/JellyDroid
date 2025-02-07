package me.cniekirk.jellydroid.feature.home

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import me.cniekirk.core.jellydroid.domain.model.HomeStructure
import me.cniekirk.core.jellydroid.domain.usecase.GetHomeStructureUseCase
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.model.LatestItems
import me.cniekirk.jellydroid.feature.home.mobile.HomeEffect
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
    fun `when initialised and getHomeStructureUseCase returns success then verify state mutated correctly`() = runTest {
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
    fun `when initialised and getHomeStructureUseCase returns error then verify ShowError effect posted correctly`() = runTest {
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