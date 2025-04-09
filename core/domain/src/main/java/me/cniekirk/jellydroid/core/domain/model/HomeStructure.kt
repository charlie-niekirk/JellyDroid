package me.cniekirk.jellydroid.core.domain.model

import me.cniekirk.jellydroid.core.model.LatestItems
import me.cniekirk.jellydroid.core.model.ResumeItem
import me.cniekirk.jellydroid.core.model.UserView

data class HomeStructure(
    val userViews: List<UserView>,
    val resumeItems: List<ResumeItem>,
    val latestItems: LatestItems
)
