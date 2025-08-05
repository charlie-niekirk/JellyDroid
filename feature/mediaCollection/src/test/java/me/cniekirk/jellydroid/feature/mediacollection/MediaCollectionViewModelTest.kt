package me.cniekirk.jellydroid.feature.mediacollection

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.cniekirk.jellydroid.core.domain.model.Media
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.views.CollectionKind
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaCollectionUseCase
import me.cniekirk.jellydroid.feature.mediacollection.model.ErrorType
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.test.test

class MediaCollectionViewModelTest {

    private val getMediaCollectionUseCase = mockk<GetMediaCollectionUseCase>()

    private lateinit var underTest: MediaCollectionViewModel

    @Before
    fun setup() {
        underTest = MediaCollectionViewModel(testCollection, getMediaCollectionUseCase)
    }

    @Test
    fun `test successful collection load updates state`() = runTest {
        coEvery { getMediaCollectionUseCase(any(), any()) } returns Ok(testItems)

        underTest.test(this) {
            runOnCreate()

            expectState { copy(isLoading = false, collectionItems = testItems) }
        }

        coVerify(exactly = 1) { getMediaCollectionUseCase(COLLECTION_ID, CollectionKind.MOVIES) }
    }

    @Test
    fun `test authentication error posts auth error effect`() = runTest {
        coEvery { getMediaCollectionUseCase(any(), any()) } returns Err(NetworkError.AuthenticationError)

        underTest.test(this) {
            runOnCreate()

            expectState { copy(isLoading = false) }
            expectSideEffect(MediaCollectionEffect.ShowError(ErrorType.AUTH_ERROR))
        }

        coVerify(exactly = 1) { getMediaCollectionUseCase(COLLECTION_ID, CollectionKind.MOVIES) }
    }

    @Test
    fun `test client or server error posts server error effect`() = runTest {
        coEvery { getMediaCollectionUseCase(any(), any()) } returns Err(NetworkError.ServerError)

        underTest.test(this) {
            runOnCreate()

            expectState { copy(isLoading = false) }
            expectSideEffect(MediaCollectionEffect.ShowError(ErrorType.SERVER_ERROR))
        }

        coVerify(exactly = 1) { getMediaCollectionUseCase(COLLECTION_ID, CollectionKind.MOVIES) }
    }

    @Test
    fun `test connection or unknown error posts network error effect`() = runTest {
        coEvery { getMediaCollectionUseCase(any(), any()) } returns Err(NetworkError.Unknown)

        underTest.test(this) {
            runOnCreate()

            expectState { copy(isLoading = false) }
            expectSideEffect(MediaCollectionEffect.ShowError(ErrorType.NETWORK_ERROR))
        }

        coVerify(exactly = 1) { getMediaCollectionUseCase(COLLECTION_ID, CollectionKind.MOVIES) }
    }

    companion object {
        private const val COLLECTION_ID = "12345"
        private const val COLLECTION_NAME = "Movies"

        private val testCollection = MediaCollection(
            collectionId = COLLECTION_ID,
            collectionName = COLLECTION_NAME,
            collectionType = CollectionType.MOVIES
        )

        private val testItems = listOf(
            Media(
                id = "1",
                name = "media",
                thumbnailUrl = "https://example.org/image.jpg"
            )
        )
    }
}
