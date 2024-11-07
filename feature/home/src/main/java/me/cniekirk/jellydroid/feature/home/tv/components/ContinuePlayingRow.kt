package me.cniekirk.jellydroid.feature.home.tv.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import kotlinx.collections.immutable.ImmutableList
import me.cniekirk.jellydroid.core.model.ResumeItem

@Composable
fun ContinuePlayingRow(
    modifier: Modifier = Modifier,
    resumeItems: ImmutableList<ResumeItem>
) {
    var isListFocused by remember { mutableStateOf(false) }
    var selectedItem by remember(resumeItems) { mutableStateOf(resumeItems.first()) }

    
}

enum class ItemDirection(val aspectRatio: Float) {
    Vertical(10.5f / 16f),
    Horizontal(16f / 9f);
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ImmersiveListRow(
    modifier: Modifier = Modifier,
    resumeItems: ImmutableList<ResumeItem>,
    itemDirection: ItemDirection = ItemDirection.Horizontal,
    startPadding: Dp = 74.dp,
    endPadding: Dp = 74.dp,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge,
    showItemTitle: Boolean = true,
    onItemSelected: (ResumeItem) -> Unit = {},
    onItemFocused: (ResumeItem) -> Unit = {}
) {
    val (lazyRow, firstItem) = remember { FocusRequester.createRefs() }

    Column(
        modifier = modifier.focusGroup()
    ) {
        if (title != null) {
            Text(
                text = title,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(start = startPadding)
                    .padding(vertical = 16.dp)
            )
        }
        AnimatedContent(
            targetState = resumeItems,
            label = "",
        ) { resumeState ->
            LazyRow(
                contentPadding = PaddingValues(start = startPadding, end = endPadding),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .focusRequester(lazyRow)
                    .focusRestorer {
                        firstItem
                    }
            ) {
                itemsIndexed(
                    resumeState,
                    key = { _, resumeItem ->
                        resumeItem.id
                    }
                ) { index, resume ->

                }
            }
        }
    }
}