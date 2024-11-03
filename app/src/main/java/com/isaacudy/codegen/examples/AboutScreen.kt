package com.isaacudy.codegen.examples

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaacudy.codegen.examples.ui.ScreenTitle

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        ScreenTitle(
            title = "About",
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = """
                This is a sample app that demonstrates how to use KSP code generation. 
                
                The `PreviewCatalogueScreen` composable is generated through KSP code generation and contains previews for all the @Preview annotated composables in the app. 
            """.trimIndent()
        )
    }
}

@Preview
@Composable
fun AboutScreenPreview() {
    AboutScreen()
}