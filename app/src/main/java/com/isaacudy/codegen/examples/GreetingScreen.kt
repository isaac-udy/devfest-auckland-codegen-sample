package com.isaacudy.codegen.examples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isaacudy.codegen.examples.ui.ScreenTitle

@Composable
fun GreetingScreen() {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        ScreenTitle("Greeting")
        Spacer(modifier = Modifier.height(8.dp))
        Greeting(name = "CodeGenExample")
    }
}