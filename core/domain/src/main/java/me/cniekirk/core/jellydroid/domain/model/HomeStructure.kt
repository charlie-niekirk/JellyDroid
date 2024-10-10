package me.cniekirk.core.jellydroid.domain.model

import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView

data class HomeStructure(
    val userViews: List<UserView>,
    val resumeItems: List<ResumeItem>
)
