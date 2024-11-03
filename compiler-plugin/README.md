# `:compiler-plugin`

This module defines the Kotlin compiler plugin that injects the `ScreenLifecycleEffect` into
`@Composable` functions that have names ending with "Screen". The `ExampleGradleSubplugin` that is
defined in the `build.gradle.kts` file of the `:app` module applies this plugin to the `:app`
module.