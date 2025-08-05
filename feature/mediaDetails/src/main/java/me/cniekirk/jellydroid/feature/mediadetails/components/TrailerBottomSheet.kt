package me.cniekirk.jellydroid.feature.mediadetails.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme
import me.cniekirk.jellydroid.core.domain.model.mediaDetails.Trailer
import me.cniekirk.jellydroid.feature.mediadetails.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrailerBottomSheetContent(
    trailers: List<Trailer>,
    onTrailerClicked: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.trailer_bottom_sheet_title),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(trailers) { trailer ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTrailerClicked(trailer.url) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = trailer.name)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TrailerBottomSheetPreview() {
    JellyDroidTheme {
        Surface {
            TrailerBottomSheetContent(
                trailers = listOf(
                    Trailer("Trailer 1", "url1"),
                    Trailer("Trailer 2", "url2")
                ),
                onTrailerClicked = {},
            )
        }
    }
}
