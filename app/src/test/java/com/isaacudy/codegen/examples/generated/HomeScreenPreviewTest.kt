package com.isaacudy.codegen.examples.generated

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.isaacudy.codegen.examples.HomeScreenPreview
import org.junit.Rule
import org.junit.Test

/**
 * This test class is generated by the generateComposePreviewTests Gradle task.
 */
class HomeScreenPreviewTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "android:Theme.Material.Light.NoActionBar",
        renderingMode = SessionParams.RenderingMode.SHRINK,
    )

    @Test
    fun snapshot() {
        paparazzi.snapshot {
            Box(
                modifier = Modifier.padding(0.dp),
            ) {
                HomeScreenPreview()
            }
        }
    }
}