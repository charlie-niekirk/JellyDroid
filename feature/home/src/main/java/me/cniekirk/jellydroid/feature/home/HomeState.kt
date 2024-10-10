package me.cniekirk.jellydroid.feature.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView

data class HomeState(
    val isLoading: Boolean = true,
    val userViews: ImmutableList<UserView> = persistentListOf(),
    val resumeItems: ImmutableList<ResumeItem> = persistentListOf(),
    val searchQuery: String = "",
    val errorDialogVisible: Boolean = false
)

sealed interface HomeEffect {


}