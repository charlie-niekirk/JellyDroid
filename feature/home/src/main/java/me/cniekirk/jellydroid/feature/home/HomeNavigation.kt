package me.cniekirk.jellydroid.feature.home

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Home

fun NavGraphBuilder.home() {
    composable<Home> {
        val viewModel = hiltViewModel<HomeViewModel>()
        HomeRoute(viewModel)
    }
}