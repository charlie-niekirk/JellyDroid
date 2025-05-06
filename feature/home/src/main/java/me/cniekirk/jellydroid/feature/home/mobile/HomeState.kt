package me.cniekirk.jellydroid.feature.home.mobile

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.jellydroid.core.model.errors.NetworkError
import me.cniekirk.jellydroid.core.model.LatestItem
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView

data class HomeState(
    val isLoading: Boolean = true,
    val userViews: ImmutableList<UserView> = persistentListOf(),
    val resumeItems: ImmutableList<ResumeItem> = persistentListOf(),
    val latestMovies: ImmutableList<LatestItem> = persistentListOf(),
    val latestShows: ImmutableList<LatestItem> = persistentListOf(),
    val searchQuery: String = "",
    val errorDialogVisible: Boolean = false
)

sealed interface HomeEffect {

    data class ShowError(val error: NetworkError) : HomeEffect
}
