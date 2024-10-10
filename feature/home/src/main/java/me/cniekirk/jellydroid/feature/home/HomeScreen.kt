package me.cniekirk.jellydroid.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.components.LoadingScreen
import me.cniekirk.jellydroid.feature.home.components.ResumeItems
import me.cniekirk.jellydroid.feature.home.components.UserViews
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
) {
    val state = viewModel.collectAsState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            else -> {}
        }
    }

    HomeScreen(
        state = state.value,
        onQueryChange = viewModel::queryChanged
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onQueryChange: (String) -> Unit
) {
    LoadingScreen(isLoading = state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text(stringResource(R.string.home_title)) },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings_content_description)
                        )
                    }
                }
            )

            UserViews(
                modifier = Modifier
                    .fillMaxWidth(),
                userViews = state.userViews
            )

            ResumeItems(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                resumeItems = state.resumeItems
            )
        }
    }
}