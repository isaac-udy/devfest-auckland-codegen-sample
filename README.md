# DevFest Auckland CodeGen Sample

This is a sample/example project that demonstrates three different ways to generate code in an
Android project using Kotlin. This is a companion project to a session at DevFest Auckland 2024.

The three methods of code generation demonstrated in this project are:

* Gradle tasks
* Annotation processing (KSP)
* Compiler plugins

## Gradle tasks

Using Gradle tasks to generate code is a very simple way to generate code in an Android project. The
downside of this method is that it is not very efficient and can be slow for large projects.
However, it's also a simple way to run one-off code generation tasks, when you don't actually want
to run the code generation with every build.

If you're interested in using Gradle tasks for code generation, you will want to look at
the [build.gradle.kts](./app/build.gradle.kts) file in the `:app` module, which defines two
different Gradle tasks that generate code; `generateGreeting` and `generateComposePreviewTests`.

### `generateGreeting`

The `generateGreeting` task generates a `GeneratedGreeting` function, which is a `@Composable`
function that displays a greeting message. The generated code is written to the `generated`
directory in the `:app` module, and can be viewed when running the app.

This task is run every time the `:app` module is compiled, but can also be run manually by running
`./gradlew :app:generateGreeting`.

### `generateComposePreviewTests`

The `generateComposePreviewTests` task generates a [Paparazzi](https://github.com/cashapp/paparazzi)
test for each `@Preview` annotated `@Composable` function in the project. The generated tests are
written to the `src/test/java` directory in the `:app` module, which means that these tests can be
committed to source control (even though they're generated). This is a good example of the
limitations of generating code in Gradle tasks (due to the string parsing required to find the
`@Preview` functions), but is also a good example of a code generation task that you probably don't
want to run every time you're building the project.

This task is run every time the `:app` module's unit tests are compiled, but can also be run
manually by running `./gradlew :app:generateComposePreviewTests`.

## Annotation processing (KSP)

Annotation processing is a more efficient way to generate code in an Android project, as it allows
you to generate code at compile time based on annotations in your code.

The `:ksp-processor` module demonstrates how to use Kotlin Symbol Processing (KSP) to generate code
in an Android project based on annotations. The `:ksp-processor` module defines a KSP processor that
creates a `PreviewCatalogueScreen` which is a `@Composable` function that displays all the
`@Preview` annotated `@Composable` functions in the project. This screen is generated at compile
time, and can be viewed in the app.

## Kotlin Compiler Plugins

Kotlin compiler plugins are the most powerful way to generate code in an Android project, as they
allow you to inject generated code directly into existing functions and classes during compilation.
However, they are also the most complex way to generate code, and the Kotlin compiler plugin API is
largely undocumented.

The `:compiler-plugin` module demonstrates how to use a Kotlin compiler plugin to inject generated
code into functions at compile time. The `:compiler-plugin` module defines the Kotlin compiler
plugin that injects the `ScreenLifecycleEffect` into `@Composable` functions that have names ending
with "Screen". The `ExampleGradleSubplugin` that is defined in the `build.gradle.kts` file of the
`:app` module applies this plugin to the `:app` module.