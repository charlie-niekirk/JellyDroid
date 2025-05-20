package me.cniekirk.jellydroid.core.domain.model.views

import me.cniekirk.jellydroid.core.domain.model.ResumeItem
import me.cniekirk.jellydroid.core.domain.model.latest.LatestItems

data class HomeStructure(
    val userViews: List<UserView>,
    val resumeItems: List<ResumeItem>,
    val latestItems: LatestItems
)