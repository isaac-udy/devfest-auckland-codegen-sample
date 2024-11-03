package com.isaacudy.codegen.examples.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@JvmName("SimpleListString")
fun SimpleList(
    items: List<String>,
    modifier: Modifier = Modifier,
) {
    SimpleList(
        items = items.map { it to null },
        modifier = modifier,
    )
}

@Composable
fun SimpleList(
    items: List<Pair<String, String?>>,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
    ) {
        items.forEach { item ->
            SimpleListItem(
                title = item.first,
                subtitle = item.second,
            )
        }
    }
}

@Composable
@Preview
fun SimpleListPreview() {
    SimpleList(
        items = listOf(
            "Item 1" to "Subtitle 1",
            "Item 2" to "Subtitle 2",
            "Item 3" to "Subtitle 3",
        )
    )
}

@Composable
@Preview
fun SimpleListPreviewNoSubtitle() {
    SimpleList(
        items = listOf(
            "Item 1",
            "Item 2",
            "Item 3",
        )
    )
}