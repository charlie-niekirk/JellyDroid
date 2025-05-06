package me.cniekirk.jellydroid.core.domain

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.domain.mapping.MediaDetailsMapper
import me.cniekirk.jellydroid.core.domain.model.AgeRating
import me.cniekirk.jellydroid.core.domain.model.CommunityRating
import me.cniekirk.jellydroid.core.domain.model.MediaAttributes
import me.cniekirk.jellydroid.core.domain.model.MediaDetailsUiModel
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import me.cniekirk.jellydroid.core.domain.usecase.GetMediaDetailsUseCase
import org.jellyfin.sdk.model.api.BaseItemDto
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.MediaType
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

class GetMediaDetailsUseCaseImplTest {

    private val jellyfinRepository = mockk<JellyfinRepository>()
    private val mediaDetailsMapper = mockk<MediaDetailsMapper>()

    private lateinit var sut: GetMediaDetailsUseCase

    @Before
    fun setup() {
        sut = GetMediaDetailsUseCase(jellyfinRepository, mediaDetailsMapper)
    }

    @Test
    fun `when invoked and success returned then verify data mapped to ui model with success`() =
        runTest {
            // Given
            coEvery { jellyfinRepository.getServerBaseUrl() } returns Ok(BASE_URL)
            coEvery { jellyfinRepository.getMediaDetails(any()) } returns Ok(baseItemDto)
            every { mediaDetailsMapper.toUiModel(any(), any()) } returns successMediaDetails

            // When
            sut.invoke(MEDIA_ID)

            // Then
            coVerify { jellyfinRepository.getMediaDetails(MEDIA_ID) }
            verify { mediaDetailsMapper.toUiModel(baseItemDto, BASE_URL) }
        }

    @Test
    fun `when invoked and error returned then verify data not mapped with error`() = runTest {
        // Given
        coEvery { jellyfinRepository.getMediaDetails(any()) } returns unknownError

        // When
        val result = sut.invoke(MEDIA_ID)

        // Then
        coVerify { jellyfinRepository.getMediaDetails(MEDIA_ID) }
        verify(exactly = 0) { mediaDetailsMapper.toUiModel(any(), any()) }
        assertEquals(unknownError, result)
    }

    companion object {
        const val MEDIA_ID = "0"
        private const val NAME = "Inception"
        private const val SYNOPSIS = "This is a synopsis"
        private const val BASE_URL = "http://192.168.1.2:8096/"

        val baseItemDto = BaseItemDto(
            id = UUID.randomUUID(),
            type = BaseItemKind.MOVIE,
            mediaType = MediaType.VIDEO,
            name = NAME,
            overview = SYNOPSIS,
            officialRating = "12"
        )

        val successMediaDetails = MediaDetailsUiModel(
            mediaId = NAME,
            synopsis = SYNOPSIS,
            primaryImageUrl = "",
            mediaAttributes = MediaAttributes(
                communityRating = CommunityRating.NoRating,
                ageRating = AgeRating("", ""),
                runtime = null
            ),
            mediaPath = ""
        )

        val unknownError = Err(NetworkError.Unknown)
    }
}
