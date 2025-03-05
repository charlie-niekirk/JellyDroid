package me.cniekirk.jellydroid.core.data.repository

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import com.github.michaelbull.result.runCatching
import kotlinx.coroutines.flow.Flow
import me.cniekirk.jellydroid.core.common.errors.LocalDataError
import me.cniekirk.jellydroid.core.common.errors.NetworkError
import me.cniekirk.jellydroid.core.data.mapping.toLatestItem
import me.cniekirk.jellydroid.core.data.mapping.toResumeItem
import me.cniekirk.jellydroid.core.data.mapping.toServer
import me.cniekirk.jellydroid.core.data.mapping.toUser
import me.cniekirk.jellydroid.core.data.mapping.toUserView
import me.cniekirk.jellydroid.core.database.dao.ServerDao
import me.cniekirk.jellydroid.core.database.dao.UserDao
import me.cniekirk.jellydroid.core.database.entity.Server
import me.cniekirk.jellydroid.core.datastore.repository.AppPreferencesRepository
import me.cniekirk.jellydroid.core.model.LatestItem
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.api.client.ApiClient
import org.jellyfin.sdk.api.client.exception.ApiClientException
import org.jellyfin.sdk.api.client.exception.InvalidContentException
import org.jellyfin.sdk.api.client.exception.InvalidStatusException
import org.jellyfin.sdk.api.client.exception.MissingBaseUrlException
import org.jellyfin.sdk.api.client.exception.MissingPathVariableException
import org.jellyfin.sdk.api.client.exception.SecureConnectionException
import org.jellyfin.sdk.api.client.exception.TimeoutException
import org.jellyfin.sdk.api.client.extensions.itemsApi
import org.jellyfin.sdk.api.client.extensions.mediaInfoApi
import org.jellyfin.sdk.api.client.extensions.userApi
import org.jellyfin.sdk.api.client.extensions.userLibraryApi
import org.jellyfin.sdk.api.client.extensions.userViewsApi
import org.jellyfin.sdk.api.client.extensions.videosApi
import org.jellyfin.sdk.api.sockets.exception.SocketException
import org.jellyfin.sdk.api.sockets.exception.SocketStoppedException
import org.jellyfin.sdk.discovery.RecommendedServerInfo
import org.jellyfin.sdk.discovery.RecommendedServerInfoScore
import org.jellyfin.sdk.model.UUID
import org.jellyfin.sdk.model.api.AuthenticateUserByName
import org.jellyfin.sdk.model.api.BaseItemDto
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
import org.jellyfin.sdk.model.api.ServerDiscoveryInfo
import org.jellyfin.sdk.model.api.SubtitleDeliveryMethod
import org.jellyfin.sdk.model.api.SubtitleProfile
import org.jellyfin.sdk.model.api.TranscodingProfile
import org.jellyfin.sdk.model.serializer.toUUID
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
class JellyfinRepositoryImpl @Inject constructor(
    private val jellyfin: Jellyfin,
    private val apiClient: ApiClient,
    private val serverDao: ServerDao,
    private val userDao: UserDao,
    private val appPreferencesRepository: AppPreferencesRepository
) : JellyfinRepository {

    override suspend fun discoverServers(): Flow<ServerDiscoveryInfo> =
        jellyfin.discovery.discoverLocalServers()

    override suspend fun connectToServer(serverDiscoveryInfo: ServerDiscoveryInfo) {
        serverDao.insertAll(serverDiscoveryInfo.toServer())
        apiClient.update(baseUrl = serverDiscoveryInfo.address)
    }

    override suspend fun connectToServer(address: String): Result<String, NetworkError> {
        return getSingleServer(address)
            .andThen { recommendedServerInfo ->
                val publicSystemInfo = recommendedServerInfo.systemInfo.getOrNull()

                val id = publicSystemInfo?.id
                val name = publicSystemInfo?.serverName

                if (id != null && name != null) {
                    serverDao.insertAll(Server(id, address, name))
                    appPreferencesRepository.setCurrentServer(id)

                    apiClient.update(baseUrl = address)

                    Ok(name)
                } else {
                    Err(NetworkError.ServerError)
                }
            }
    }

    private suspend fun getSingleServer(address: String): Result<RecommendedServerInfo, NetworkError> {
        return safeApiCall {
            val servers = jellyfin.discovery.getRecommendedServers(address, RecommendedServerInfoScore.GOOD)
            if (servers.isNotEmpty()) {
                servers.first()
            } else {
                throw InvalidContentException()
            }
        }
    }

    override suspend fun authenticateUser(username: String, password: String): Result<Unit, NetworkError> {
        return safeApiCall {
            val authResult by apiClient.userApi.authenticateUserByName(
                data = AuthenticateUserByName(
                    username = username,
                    pw = password
                )
            )

            val user = authResult.toUser()

            userDao.insertAll(user)
            appPreferencesRepository.setLoggedInUser(user.userId)
            apiClient.update(accessToken = authResult.accessToken)
        }
    }

    override suspend fun getUserViews(): Result<List<UserView>, NetworkError> {
        return safeApiCall { apiClient.userViewsApi.getUserViews() }
            .andThen { response ->
                val serverUrl = apiClient.baseUrl
                val data = response.content.items

                if (serverUrl != null) {
                    Ok(data?.map { it.toUserView(serverUrl) } ?: listOf())
                } else {
                    Err(NetworkError.Unknown)
                }
            }
    }

    override suspend fun getCurrentServerAndUser(): Result<String, LocalDataError> {
        val currentServerId = appPreferencesRepository.getCurrentServer()
        val server = serverDao.getAllServersWithUsers()
        val serverWithUsers = server.firstOrNull { it.server.serverId.equals(currentServerId, true) }

        return if (serverWithUsers != null) {
            val defaultUser = serverWithUsers.users.first()

            apiClient.update(accessToken = defaultUser.accessToken, baseUrl = serverWithUsers.server.baseUrl)

            Ok(defaultUser.userId)
        } else {
            Err(LocalDataError.ServerNotExists)
        }
    }

    override suspend fun getServerBaseUrl(): Result<String, NetworkError> {
        val baseUrl = apiClient.baseUrl
        return if (baseUrl != null) {
            Ok(baseUrl)
        } else {
            Err(NetworkError.AuthenticationError)
        }
    }

    override suspend fun getContinuePlayingItems(): Result<List<ResumeItem>, NetworkError> {
        return safeApiCall {
            apiClient.itemsApi.getResumeItems(
                userId = appPreferencesRepository.getLoggedInUser().toUUID(),
                limit = 12,
                includeItemTypes = listOf(BaseItemKind.MOVIE, BaseItemKind.EPISODE)
            )
        }.map { response ->
            response.content.items?.map {
                it.toResumeItem(apiClient.baseUrl)
            } ?: listOf()
        }
    }

    override suspend fun getLatestMovies(): Result<List<LatestItem>, NetworkError> {
        return safeApiCall {
            apiClient.userLibraryApi.getLatestMedia(
                userId = appPreferencesRepository.getLoggedInUser().toUUID(),
                limit = 12,
                includeItemTypes = listOf(BaseItemKind.MOVIE)
            )
        }.map { response ->
            response.content.map { it.toLatestItem(apiClient.baseUrl) }
        }
    }

    override suspend fun getLatestShows(): Result<List<LatestItem>, NetworkError> {
        return safeApiCall {
            apiClient.userLibraryApi.getLatestMedia(
                userId = appPreferencesRepository.getLoggedInUser().toUUID(),
                limit = 12,
                includeItemTypes = listOf(BaseItemKind.SERIES)
            )
        }.map { response ->
            response.content.map { it.toLatestItem(apiClient.baseUrl) }
        }
    }

    override suspend fun getMediaDetails(mediaId: String): Result<BaseItemDto, NetworkError> {
        return safeApiCall {
            apiClient.userLibraryApi.getItem(
                UUID.fromString(mediaId),
                userId = appPreferencesRepository.getLoggedInUser().toUUID()
            ).content
        }
    }

    override suspend fun getMediaCollection(collectionId: String): Result<List<BaseItemDto>, NetworkError> {
        return safeApiCall {
            apiClient.itemsApi.getItems(
                userId = appPreferencesRepository.getLoggedInUser().toUUID(),
                limit = 12,
                includeItemTypes = listOf(BaseItemKind.MOVIE)
            ).content.items ?: listOf()
        }
    }

    @Suppress("LongMethod")
    override suspend fun getPlaybackInfo(
        mediaSourceId: String,
        startTimeTicks: Int,
        audioStreamIndex: Int,
        subtitleStreamIndex: Int,
        maxStreamingBitrate: Int
    ): Result<String, NetworkError> {
        return safeApiCall {
            val response = apiClient.mediaInfoApi.getPostedPlaybackInfo(
                itemId = mediaSourceId.toUUID(),
                data = PlaybackInfoDto(
                    userId = appPreferencesRepository.getLoggedInUser().toUUID(),
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

    private suspend fun <T> safeApiCall(block: suspend () -> T): Result<T, NetworkError> {
        return runCatching {
            block()
        }.mapError { throwable ->
            Timber.e(throwable)
            when (throwable) {
                is ApiClientException -> {
                    when (throwable) {
                        is SocketStoppedException,
                        is SocketException,
                        is SecureConnectionException,
                        is TimeoutException -> {
                            NetworkError.ConnectionError
                        }
                        is MissingPathVariableException, is MissingBaseUrlException -> {
                            NetworkError.ClientConfigurationError
                        }
                        is InvalidStatusException, is InvalidContentException -> {
                            NetworkError.ServerError
                        }
                        else -> NetworkError.Unknown
                    }
                }
                else -> NetworkError.Unknown
            }
        }
    }
}
