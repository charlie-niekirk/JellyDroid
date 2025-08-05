package me.cniekirk.jellydroid.feature.library

import me.cniekirk.jellydroid.core.domain.model.views.UserView

data class LibraryState(
    val isLoading: Boolean = true,
    val mediaLibraries: List<UserView> = listOf()
)
