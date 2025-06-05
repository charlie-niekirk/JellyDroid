package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import me.cniekirk.jellydroid.core.data.mapping.MediaDetailsMapper
import me.cniekirk.jellydroid.core.data.mapping.toLatestItem
import me.cniekirk.jellydroid.core.data.mapping.toResumeItem
import me.cniekirk.jellydroid.core.data.mapping.toServerAndUsers
import me.cniekirk.jellydroid.core.data.mapping.toUserView
import me.cniekirk.jellydroid.core.data.safeApiCall
import me.cniekirk.jellydroid.core.database.dao.ServerDao
import me.cniekirk.jellydroid.core.domain.model.ResumeItem
import me.cniekirk.jellydroid.core.domain.model.error.LocalDataError
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.latest.LatestItem
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.MediaDetails
import me.cniekirk.jellydroid.core.domain.model.servers.ServerAndUsers
import me.cniekirk.jellydroid.core.domain.model.views.UserView
import me.cniekirk.jellydroid.core.domain.repository.JellyfinRepository
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.api.client.extensions.mediaInfoApi
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.api.client.extensions.userViewsApi
import org.jellyfin.sdk.api.client.extensions.videosApi
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.BaseItemKind
import org.jellyfin.sdk.model.api.CodecProfile
import org.jellyfin.sdk.model.api.CodecType
import org.jellyfin.sdk.model.api.DeviceProfile
import org.jellyfin.sdk.model.api.DirectPlayProfile
import org.jellyfin.sdk.model.api.DlnaProfileType
import org.jellyfin.sdk.model.api.EncodingContext
import org.jellyfin.sdk.model.api.MediaStreamProtocol
import org.jellyfin.sdk.model.api.PlaybackInfoDto
import org.jellyfin.sdk.model.api.ProfileCondition
import org.jellyfin.sdk.model.api.ProfileConditionType
import org.jellyfin.sdk.model.api.ProfileConditionValue
import org.jellyfin.sdk.model.api.SubtitleDeliveryMethod
import org.jellyfin.sdk.model.api.SubtitleProfile
import org.jellyfin.sdk.model.api.TranscodingProfile
import org.jellyfin.sdk.model.serializer.toUUID
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
internal class JellyfinRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val serverDao: ServerDao,
    private val mediaDetailsMapper: MediaDetailsMapper
) : JellyfinRepository {

    override suspend fun getUserFromToken(): Result<String, NetworkError> {
        return safeApiCall { apiClient.userApi.getCurrentUser() }
            .map { it.content.id.toString() }
    }

    override suspend fun getUserViews(): Result<List<UserView>, NetworkError> {
        return safeApiCall { apiClient.userViewsApi.getUserViews() }
            .andThen { response ->
                val serverUrl = apiClient.baseUrl
                val data = response.content.items

                if (serverUrl != null) {
                    Ok(data?.mapNotNull { it.toUserView(serverUrl) } ?: listOf())
                } else {
                    Err(NetworkError.Unknown)
                }
            }
    }

    override suspend fun getServerWithUsersById(serverId: String): Result<ServerAndUsers, LocalDataError> {
        val server = serverDao.getAllServersWithUsers()
        val serverWithUsers = server.firstOrNull { it.server.serverId.equals(serverId, true) }

        return if (serverWithUsers != null) {
            Ok(serverWithUsers.toServerAndUsers())
        } else {
            Err(LocalDataError.ServerNotExists)
        }
    }

    override suspend fun updateClient(baseUrl: String, accessToken: String) {
        apiClient.update(accessToken = accessToken, baseUrl = baseUrl)
    }

    override suspend fun getServerBaseUrl(): Result<String, NetworkError> {
        val baseUrl = apiClient.baseUrl
        return if (baseUrl != null) {
            Ok(baseUrl)
        } else {
            Err(NetworkError.AuthenticationError)
        }
    }

    override suspend fun getContinuePlayingItems(loggedInUserId: String): Result<List<ResumeItem>, NetworkError> {
        return safeApiCall {
            apiClient.itemsApi.getResumeItems(
                userId = loggedInUserId.toUUID(),
                limit = 12,
                includeItemTypes = listOf(BaseItemKind.MOVIE, BaseItemKind.EPISODE)
            )
        }.map { response ->
            response.content.items?.map {
                it.toResumeItem(apiClient.baseUrl)
            } ?: listOf()
        }
    }

    override suspend fun getLatestMovies(loggedInUserId: String): Result<List<LatestItem>, NetworkError> {
        return safeApiCall {
            apiClient.userLibraryApi.getLatestMedia(
                userId = loggedInUserId.toUUID(),
                limit = 12,
                includeItemTypes = listOf(BaseItemKind.MOVIE)
            )
        }.map { response ->
            response.content.map { it.toLatestItem(apiClient.baseUrl) }
        }
    }

    override suspend fun getLatestShows(loggedInUserId: String): Result<List<LatestItem>, NetworkError> {
        return safeApiCall {
            apiClient.userLibraryApi.getLatestMedia(
                userId = loggedInUserId.toUUID(),
                limit = 12,
                includeItemTypes = listOf(BaseItemKind.SERIES)
            )
        }.map { response ->
            response.content.map { it.toLatestItem(apiClient.baseUrl) }
        }
    }

    override suspend fun getMediaDetails(mediaId: String, loggedInUserId: String): Result<MediaDetails, NetworkError> {
        return safeApiCall {
            val content = apiClient.userLibraryApi.getItem(
                UUID.fromString(mediaId),
                userId = loggedInUserId.toUUID()
            ).content

            mediaDetailsMapper.toMediaDetails(content, apiClient.baseUrl)
        }
    }

    @Suppress("LongMethod")
    override suspend fun getPlaybackInfo(
        mediaSourceId: String,
        startTimeTicks: Int,
        audioStreamIndex: Int,
        subtitleStreamIndex: Int,
        maxStreamingBitrate: Int,
        loggedInUserId: String
    ): Result<String, NetworkError> {
        return safeApiCall {
            val response = apiClient.mediaInfoApi.getPostedPlaybackInfo(
                itemId = mediaSourceId.toUUID(),
                data = PlaybackInfoDto(
                    userId = loggedInUserId.toUUID(),
                    startTimeTicks = startTimeTicks.toLong(),
                    maxStreamingBitrate = maxStreamingBitrate,
                    deviceProfile = DeviceProfile(
                        name = "Direct play all",
                        maxStaticBitrate = 1_000_000_000,
                        maxStreamingBitrate = 1_000_000_000,
                        codecProfiles = listOf(
                            CodecProfile(
                                type = CodecType.VIDEO_AUDIO,
                                codec = "aac",
                                conditions = listOf(
                                    ProfileCondition(
                                        condition = ProfileConditionType.EQUALS,
                                        property = ProfileConditionValue.IS_SECONDARY_AUDIO,
                                        value = "false",
                                        isRequired = false
                                    )
                                ),
                                applyConditions = listOf()
                            ),
                            CodecProfile(
                                type = CodecType.VIDEO_AUDIO,
                                conditions = listOf(
                                    ProfileCondition(
                                        condition = ProfileConditionType.EQUALS,
                                        property = ProfileConditionValue.IS_SECONDARY_AUDIO,
                                        value = "false",
                                        isRequired = false
                                    )
                                ),
                                applyConditions = listOf()
                            ),
                            CodecProfile(
                                type = CodecType.VIDEO,
                                codec = "h264",
                                conditions = listOf(
                                    ProfileCondition(
                                        condition = ProfileConditionType.NOT_EQUALS,
                                        property = ProfileConditionValue.IS_ANAMORPHIC,
                                        value = "true",
                                        isRequired = false
                                    ),
                                    ProfileCondition(
                                        condition = ProfileConditionType.EQUALS_ANY,
                                        property = ProfileConditionValue.VIDEO_PROFILE,
                                        value = "main|baseline",
                                        isRequired = false
                                    ),
                                    ProfileCondition(
                                        condition = ProfileConditionType.EQUALS_ANY,
                                        property = ProfileConditionValue.VIDEO_RANGE_TYPE,
                                        value = "SDR",
                                        isRequired = false
                                    ),
                                    ProfileCondition(
                                        condition = ProfileConditionType.LESS_THAN_EQUAL,
                                        property = ProfileConditionValue.VIDEO_LEVEL,
                                        value = "52",
                                        isRequired = false
                                    ),
                                    ProfileCondition(
                                        condition = ProfileConditionType.NOT_EQUALS,
                                        property = ProfileConditionValue.IS_INTERLACED,
                                        value = "true",
                                        isRequired = false
                                    ),
                                ),
                                applyConditions = listOf()
                            ),
                            CodecProfile(
                                type = CodecType.VIDEO,
                                codec = "hevc",
                                conditions = listOf(
                                    ProfileCondition(
                                        condition = ProfileConditionType.NOT_EQUALS,
                                        property = ProfileConditionValue.IS_ANAMORPHIC,
                                        value = "true",
                                        isRequired = false
                                    ),
                                    ProfileCondition(
                                        condition = ProfileConditionType.EQUALS_ANY,
                                        property = ProfileConditionValue.VIDEO_PROFILE,
                                        value = "main|main 10",
                                        isRequired = false
                                    ),
                                    ProfileCondition(
                                        condition = ProfileConditionType.EQUALS_ANY,
                                        property = ProfileConditionValue.VIDEO_RANGE_TYPE,
                                        value = "SDR|HDR10|HLG",
                                        isRequired = false
                                    ),
                                    ProfileCondition(
                                        condition = ProfileConditionType.LESS_THAN_EQUAL,
                                        property = ProfileConditionValue.VIDEO_LEVEL,
                                        value = "183",
                                        isRequired = false
                                    ),
                                    ProfileCondition(
                                        condition = ProfileConditionType.NOT_EQUALS,
                                        property = ProfileConditionValue.IS_INTERLACED,
                                        value = "true",
                                        isRequired = false
                                    ),
                                ),
                                applyConditions = listOf()
                            ),
                            CodecProfile(
                                type = CodecType.VIDEO,
                                codec = "vp9",
                                conditions = listOf(
                                    ProfileCondition(
                                        condition = ProfileConditionType.EQUALS_ANY,
                                        property = ProfileConditionValue.VIDEO_RANGE_TYPE,
                                        value = "SDR",
                                        isRequired = false
                                    )
                                ),
                                applyConditions = listOf()
                            ),
                        ),
                        containerProfiles = emptyList(),
                        directPlayProfiles = listOf(
                            DirectPlayProfile(
                                container = "mp4",
                                type = DlnaProfileType.VIDEO,
                                audioCodec = "aac,flac,mp3,vorbis",
                                videoCodec = "hevc,h264,h263",
                            ),
                            DirectPlayProfile(
                                container = "mkv",
                                type = DlnaProfileType.VIDEO,
                                audioCodec = "mp3,vorbis,opus",
                                videoCodec = "hevc,h264,h263,vp8,vp9",
                            ),
                            DirectPlayProfile(
                                container = "m4a",
                                type = DlnaProfileType.AUDIO,
                                audioCodec = "aac,flac,mp3,vorbis"
                            ),
                            DirectPlayProfile(
                                container = "hls",
                                type = DlnaProfileType.VIDEO,
                                audioCodec = "aac,flac",
                                videoCodec = "hevc,h264",
                            ),
                        ),
                        transcodingProfiles = listOf(
                            TranscodingProfile(
                                container = "mp4",
                                type = DlnaProfileType.VIDEO,
                                audioCodec = "aac,flac,mp3,vorbis",
                                videoCodec = "hevc,h264,h263",
                                context = EncodingContext.STREAMING,
                                protocol = MediaStreamProtocol.HLS,
                                maxAudioChannels = "2",
                                minSegments = 1,
                                breakOnNonKeyFrames = true,
                                conditions = emptyList()
                            )
                        ),
                        subtitleProfiles = listOf(
                            SubtitleProfile("srt", SubtitleDeliveryMethod.EXTERNAL),
                            SubtitleProfile("ass", SubtitleDeliveryMethod.EXTERNAL),
                            SubtitleProfile("vtt", SubtitleDeliveryMethod.EXTERNAL),
                            SubtitleProfile("ssa", SubtitleDeliveryMethod.EXTERNAL)
                        ),

                    ),
                )
            ).content

            Timber.d("Response: $response")

            apiClient.baseUrl + response.mediaSources.first().transcodingUrl
        }
    }

    override suspend fun getStreamUrl(mediaSourceId: String): Result<String, NetworkError> {
        return safeApiCall {
            apiClient.videosApi.getVideoStreamUrl(
                itemId = mediaSourceId.toUUID(),
                static = true,
                mediaSourceId = mediaSourceId
            )
        }
    }
}
