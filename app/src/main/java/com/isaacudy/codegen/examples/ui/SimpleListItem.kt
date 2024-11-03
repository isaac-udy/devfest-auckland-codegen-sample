package com.isaacudy.codegen.examples.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SimpleListItem(
    title: String,
    subtitle: String? = null,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                shape = MaterialTheme.shapes.medium
            )
            .let {
                if (onClick != null) {
                    it.clickable { onClick() }
                } else {
                    it
                }
            }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview
fun SimpleListItemPreview() {
    SimpleListItem(
        title = "Title",
        subtitle = "Subtitle"
    )
}
