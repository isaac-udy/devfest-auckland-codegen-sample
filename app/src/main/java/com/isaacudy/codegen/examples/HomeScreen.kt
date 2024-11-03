package com.isaacudy.codegen.examples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaacudy.codegen.examples.ui.ScreenTitle
import com.isaacudy.codegen.examples.ui.SimpleListItem

@Composable
fun HomeScreen() {
    val navigation = LocalNavigationContainer.current
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ScreenTitle(
            title = "Home",
        )
        Spacer(modifier = Modifier.height(8.dp))
        SimpleListItem(
            title = "About",
            onClick = {
                navigation.push(Destination.About())
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        SimpleListItem(
            title = "Greeting",
            onClick = {
                navigation.push(Destination.Greeting())
            }
        )
        Spacer(modifier = Modifier.height(4.dp))
        SimpleListItem(
            title = "Greeting (generated)",
            onClick = {
                navigation.push(Destination.GeneratedGreeting())
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        SimpleListItem(
            title = "Preview Catalogue",
            onClick = {
                navigation.push(Destination.PreviewCatalogue())
            }
        )
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}