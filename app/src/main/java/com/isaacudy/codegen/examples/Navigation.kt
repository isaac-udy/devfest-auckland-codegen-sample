package com.isaacudy.codegen.examples

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.UUID

val LocalNavigationContainer = compositionLocalOf<NavigationContainer> {
    object : NavigationContainer {
        override val backstack: List<Destination> = emptyList()
        override fun push(destination: Destination) {}
        override fun pop() {}
    }
}

interface NavigationContainer {
    val backstack: List<Destination>
    fun push(destination: Destination)
    fun pop()
}

class NavigationContainerImpl(root: Destination) : NavigationContainer {
    override var backstack by mutableStateOf<List<Destination>>(listOf(root))
        private set

    override fun push(destination: Destination) {
        backstack = backstack + destination
    }

    override fun pop() {
        backstack = backstack.dropLast(1)
    }
}

@Composable
fun NavigationContainer(
    root: Destination,
    content: @Composable (Destination) -> Unit
) {
    val container = remember { NavigationContainerImpl(root) }
    CompositionLocalProvider(
        LocalNavigationContainer provides container
    ) {
        BackHandler(
            enabled = container.backstack.size > 1
        ) {
            container.pop()
        }
        AnimatedContent(
            targetState = container.backstack.lastOrNull(),
            contentKey = { it?.id }
        ) { destination ->
            key(destination?.id) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    if (destination != null) {
                        content(destination)
                    }
                }
            }
        }
    }
}

sealed class Destination {
    val id = UUID.randomUUID().toString()

    class Home : Destination()
    class PreviewCatalogue : Destination()
    class About : Destination()
    class Greeting : Destination()
    class GeneratedGreeting : Destination()
}