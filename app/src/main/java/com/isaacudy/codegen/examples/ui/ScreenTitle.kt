package com.isaacudy.codegen.examples.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScreenTitle(
    title: String,
    subtitle: String? = null,
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview
fun ScreenTitlePreview() {
    ScreenTitle(
        title = "Screen Title",
        subtitle = "Screen Subtitle"
    )
}