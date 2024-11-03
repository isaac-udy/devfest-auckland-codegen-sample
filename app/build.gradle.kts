import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.paparazzi)
}
plugins.apply(ExampleGradleSubplugin::class)

android {
    namespace = "com.isaacudy.codegen.examples"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.isaacudy.codegen.examples"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    buildFeatures {
        compose = true
    }

    sourceSets {
        getByName("main").java.srcDirs("build/generated/gradle/src/main/java")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(projects.compilerPluginCompanion)
    ksp(projects.kspProcessor)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// =================================================================================================
// Generate code from Gradle task
// =================================================================================================
/**
 * This is a simple example of a Gradle task that writes some code to a file.
 *
 * It's important to note line 46 in this file (inside the "android" block), where we're adding
 * the generated source set "build/generated/gradle/src/main/java" as a source directory for
 * the "main" source set. This is necessary so that the generated code will be compiled as
 * part of the main source of the project.
 *
 * It's also important to note the "afterEvaluate" block underneath this task's definition,
 * where we're configuring the "compileDebugKotlin" and "compileReleaseKotlin" tasks to depend on
 * this task. This is necessary so that this task is able to write the generated code before
 * the main source code is compiled, which means that the generated code will be compiled.
 */
tasks.register("generateGreeting") {
    doLast {
        // Ensure that the directory we want to write our generated code to exists
        val generatedDirectory =
            project.file("build/generated/gradle/src/main/java/com/isaacudy/codegen/examples")
                .apply {
                    // If we're already generated code, delete it
                    if (exists()) deleteRecursively()
                    mkdirs()
                }
        // Ensure that the file we want to write our generated code to exists
        val file = File(generatedDirectory, "GeneratedGreeting.kt").apply {
            createNewFile()
        }
        // This is a very simple example of code generation. We don't need to do anything fancy,
        // we're just going to output some valid code into a file. If this doesn't compile properly,
        // the build will fail, and we'll see the error in the build output, and we can adjust this
        // code accordingly.
        file.writeText(
            """
            package com.isaacudy.codegen.examples
            
            import androidx.compose.material3.Text
            import androidx.compose.runtime.Composable
            
            @Composable
            fun GeneratedGreeting() {
                Text(
                    text = "Hello from Gradle code generation!",
                )
            }
        """.trimIndent()
        )
    }
}

/**
 * This block configures the "compileDebugKotlin" and "compileReleaseKotlin" tasks to depend on
 * the "generateGreeting" task. This is necessary so that the generated code is written before
 * the main source code is compiled, which means that the generated code will be compiled.
 * We need to use "afterEvaluate" here because these tasks are not available until after the
 * build script has been evaluated for the first time. These are added through the
 * android plugin (in this case, the application plugin, but it's the same for the library plugin).
 */
afterEvaluate {
    tasks.named("compileDebugKotlin").configure {
        dependsOn("generateGreeting")
    }
    tasks.named("compileReleaseKotlin").configure {
        dependsOn("generateGreeting")
    }
}
// =================================================================================================


// =================================================================================================
// Generate tests for Compose previews from Gradle task
// =================================================================================================

/**
 * The generateComposePreviewTests task generates a test class for each @Composable @Preview function,
 * and that test class defines a paparazzi snapshot test for that preview function. Unlike the
 * generateGreeting task, this task is used to generate test classes, so it's configured to run
 * before the compileDebugUnitTestKotlin and compileReleaseUnitTestKotlin tasks. This means that
 * this task will not run if you're running the app, but it will run if you're running the tests.
 *
 * This is a more complex example of using Gradle to perform code generation. For this example,
 * we're doing the boilerplate task registration and afterEvaluate bits first, and then we're
 * defining the actual task implementation in a separate function (generateComposePreviewTests).
 *
 * If you're not sure about the task registration or afterEvaluate bits, you can refer to the
 * "generateGreeting" task above, which is a simpler example and describes what these do.
 */
tasks.register("generateComposePreviewTests") {
    doLast {
        generateComposePreviewTests(project)
    }
}

afterEvaluate {
    tasks.named("compileDebugUnitTestKotlin").configure {
        dependsOn("generateComposePreviewTests")
    }
    tasks.named("compileReleaseUnitTestKotlin").configure {
        dependsOn("generateComposePreviewTests")
    }
}

/**
 * This function generates and writes test classes for each Composable preview function
 * in the project. This is where we're going to start to see the limitations of using Gradle
 * to perform code generation, as we're going to need to scan through all the files in the
 * project, and use basic string manipulation to find the Composable preview functions,
 * which is not particularly robust.
 */
fun generateComposePreviewTests(project: Project) {
    // Recursively list all files in the "src/main/java" directory,
    // and filter these so we're only getting the Kotlin files
    val projectFiles = project.file("src/main/java")
        .listFilesRecursively()
        .filter { it.extension == "kt" }

    // For each file, find all the Composable preview functions, and store these in a list.
    // The main interesting part of this function is the getComposablePreviews function,
    // which scans through a file and finds all the Composable preview functions, and also
    // clearly shows the limitations of using Gradle for code generation.
    val composablePreviews = projectFiles.flatMap { file ->
        getComposablePreviews(file)
    }

    // Get the directory where we want to write the generated test classes, and ensure that
    // it exists. If it already exists, we delete it and recreate it (because we don't want
    // to keep the old test classes around if the Composable preview functions have changed).
    val generatedDirectory = project.file("src/test/java/com/isaacudy/codegen/examples/generated")
        .apply {
            if (exists()) deleteRecursively()
            mkdirs()
        }

    // Just like the generateGreeting task, we're going to take a simple approach here, and just
    // write the generated code as a string. In a more complex project, you'd likely use something
    // like KotlinPoet to generate the code.
    composablePreviews.forEach {
        val file = File(generatedDirectory, "${it.previewFunction}Test.kt").apply {
            createNewFile()
        }

        // We consider a preview function to be a screen if it contains "Screen" and does not
        // start with "Screen", because there are some functions like "ScreenTitle" which are
        // not screens, but contain "Screen" in their name.
        val isScreen = it.previewFunction.contains("Screen")
                && !it.previewFunction.startsWith("Screen")

        val paddingAmount = when {
            isScreen -> "0.dp"
            else -> "16.dp"
        }
        file.writeText(
            """
            package com.isaacudy.codegen.examples.generated
            
            import androidx.compose.foundation.layout.Box
            import androidx.compose.foundation.layout.padding
            import androidx.compose.ui.Modifier
            import androidx.compose.ui.unit.dp
            import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
            import app.cash.paparazzi.Paparazzi
            import com.android.ide.common.rendering.api.SessionParams
            import org.junit.Test
            import org.junit.Rule
            
            import ${it.packageName}.${it.previewFunction}
            
            /**
             * This test class is generated by the generateComposePreviewTests Gradle task.
             */
            class ${it.previewFunction}Test {
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
                            modifier = Modifier.padding($paddingAmount),
                        ) {
                            ${it.previewFunction}()
                        }
                    }
                }
            }
            """.trimIndent()
        )
    }
}

/**
 * This function scans through a file and finds all the Composable preview functions in that file.
 * This function works by reading the file line by line, and looking for lines that start with
 * "@Preview". When it finds a line that starts with "@Preview", it looks for the first line that
 * starts with "fun", and extracts the function name from that line. This is not a robust way to
 * find Composable preview functions, but it's a simple way to demonstrate how you might do this
 * using Gradle. This starts to show the limitations of using Gradle for code generation.
 */
fun getComposablePreviews(file: File): List<ComposablePreview> {
    val fileLines = file.readLines()

    // Find the first line that starts with "package ", and extract the package name from that line
    val packageName = fileLines
        .firstOrNull { it.startsWith("package ") }
        ?.substringAfter("package ")?.trim() ?: ""

    val previews = mutableListOf<ComposablePreview>()
    fileLines.forEachIndexed { index, line ->
        if (line.startsWith("@Preview")) {
            val previewFunction = fileLines.subList(index, fileLines.size)
                .takeWhile {
                    it.startsWith("@Preview") ||
                            it.startsWith("@Composable") ||
                            it.startsWith("fun ") ||
                            it.isBlank()
                }
                .firstOrNull { it.trim().startsWith("fun") }
                ?.substringAfter("fun ")
                ?.substringBefore("(")
                ?.trim() ?: return@forEachIndexed

            previews.add(
                ComposablePreview(
                    packageName = packageName,
                    previewFunction = previewFunction
                )
            )
        }
    }
    return previews
}

/**
 * This is a simple helper extension function on File is used to recursively list all files
 * in a directory, because the built-in listFiles() function only lists the files in the current
 * directory, and not the files in subdirectories.
 */
fun File.listFilesRecursively(): List<File> {
    return listFiles().orEmpty().flatMap {
        if (it.isDirectory) {
            it.listFilesRecursively()
        } else {
            listOf(it)
        }
    }
}

/**
 * This data class represents a Composable preview function, and is used to store the package name\
 * and preview function name for each Composable preview function in the project.
 */
data class ComposablePreview(
    val packageName: String,
    val previewFunction: String,
)
// =================================================================================================


// =================================================================================================
// Configure example compiler plugin
// =================================================================================================
class ExampleGradleSubplugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project) {}

    override fun getCompilerPluginId(): String = "com.isaacudy.codegen.examples.plugin"

    // When creating a compiler plugin, we need to provide a artifact for the plugin, which
// can be resolved by Gradle to download and apply the plugin. We're providing a made up
// groupId/artifactId/version here, because we actually want to use a local plugin project,
// and then down below we're going to configure dependency substitution to use the local plugin
// project instead of this made up plugin artifact.
    override fun getPluginArtifact(): SubpluginArtifact =
        SubpluginArtifact(
            groupId = "com.isaacudy.codegen.examples",
            artifactId = "example-compiler-plugin",
            version = "0.0.1",
        )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    // We don't need to provide any options to the plugin, so we just return an empty list.
// If we did have options, we would return a list of SubpluginOption instances here,
// which we could then read in the compiler plugin.
// If you're interested in how to provide options to a compiler plugin, you can check out
// https://github.com/ZacSweers/redacted-compiler-plugin, which is a good example, specifically:
// Setting options: https://github.com/ZacSweers/redacted-compiler-plugin/blob/a6d909fc00e4a9404013e10e1226a9344d882a4e/redacted-compiler-plugin-gradle/src/main/kotlin/dev/zacsweers/redacted/gradle/RedactedGradleSubplugin.kt#L61
// and
// Reading options: https://github.com/ZacSweers/redacted-compiler-plugin/blob/a6d909fc00e4a9404013e10e1226a9344d882a4e/redacted-compiler-plugin/src/main/kotlin/dev/zacsweers/redacted/compiler/RedactedPlugin.kt#L42
    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project
        return project.provider {
            listOf()
        }
    }
}

// See the comment on ExampleGradleSubplugin.getPluginArtifact
configurations.configureEach {
    resolutionStrategy.dependencySubstitution {
        substitute(module("com.isaacudy.codegen.examples:example-compiler-plugin"))
            .using(project(":compiler-plugin"))
    }
}

// =================================================================================================