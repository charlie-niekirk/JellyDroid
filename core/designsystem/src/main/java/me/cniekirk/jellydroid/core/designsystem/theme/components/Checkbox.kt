package me.cniekirk.jellydroid.core.designsystem.theme.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.cniekirk.jellydroid.core.designsystem.theme.JellyDroidTheme

@Composable
fun CheckboxItem(
    modifier: Modifier = Modifier,
    text: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChanged(it) }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview
@Composable
private fun CheckboxItemPreview() {
    JellyDroidTheme {
        Surface {
            CheckboxItem(
                modifier = Modifier.fillMaxWidth(),
                text = "Here is some very long checkbox text to see what multiline behaviour " +
                    "looks like and here is some more text again",
                checked = true,
                onCheckedChanged = {}
            )
        }
    }
}
