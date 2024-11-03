package com.isaacudy.codegen.example.plugin

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

/**
 * This function is added to all @Composable methods which have function names ending with "Screen",
 * through the "ExampleTransformer" in the ":compiler-plugin" module. This function logs the
 * name of the screen (provided through the [screenName] parameter) when the screen becomes visible,
 * and then logs the name of the screen when the screen is no longer visible,
 * based on a DisposableEffect.
 */
@Composable
fun ScreenLifecycleEffect(
    screenName: String
) {
    DisposableEffect(screenName) {
        Log.i("Screen Lifecycle", "Show: $screenName")
        onDispose {
            Log.i("Screen Lifecycle", "Hide: $screenName")
        }
    }
}