package com.isaacudy.codegen.examples

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isaacudy.codegen.examples.ui.theme.CodegenexamplesTheme

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CodegenexamplesTheme {
        Greeting("Android")
    }
}