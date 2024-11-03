package com.isaacudy.codegen.examples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.isaacudy.codegen.examples.generated.PreviewCatalogueScreen
import com.isaacudy.codegen.examples.ui.theme.CodegenexamplesTheme
import com.isaacudy.codegen.examples.ui.theme.PurpleGrey40


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(PurpleGrey40.toArgb()),
            navigationBarStyle = SystemBarStyle.light(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            ),
        )
        setContent {
            CodegenexamplesTheme(darkTheme = false) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .navigationBarsPadding()
                    ) {
                        MainNavigation()
                    }
                }
            }
        }
    }
}

private val rootNavigationDestination = Destination.Home()

@Composable
fun MainNavigation() {
    NavigationContainer(
        root = rootNavigationDestination,
    ) { destination ->
        when (destination) {
            is Destination.Home -> HomeScreen()
            is Destination.PreviewCatalogue -> PreviewCatalogueScreen()
            is Destination.About -> AboutScreen()
            is Destination.Greeting -> GreetingScreen()
            is Destination.GeneratedGreeting -> GeneratedGreetingScreen()
        }
    }
}