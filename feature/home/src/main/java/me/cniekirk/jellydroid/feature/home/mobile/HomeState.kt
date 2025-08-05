package me.cniekirk.jellydroid.feature.home.mobile

import me.cniekirk.jellydroid.core.domain.model.ResumeItem
import me.cniekirk.jellydroid.core.domain.model.error.NetworkError
import me.cniekirk.jellydroid.core.domain.model.latest.LatestItem
import me.cniekirk.jellydroid.core.domain.model.views.UserView

data class HomeState(
    val isLoading: Boolean = true,
    val userProfileImage: String = "",
    val userViews: List<UserView> = emptyList(),
    val resumeItems: List<ResumeItem> = emptyList(),
    val latestMovies: List<LatestItem> = emptyList(),
    val latestShows: List<LatestItem> = emptyList(),
    val searchQuery: String = "",
    val errorDialogVisible: Boolean = false
)

sealed interface HomeEffect {

    data class ShowError(val error: NetworkError) : HomeEffect
}
