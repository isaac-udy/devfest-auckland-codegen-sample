package com.isaacudy.codegen.examples.plugin

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.languageVersionSettings
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension.Factory

/**
 * This is the generic boilerplate required for a compiler plugin. A CompilerPluginRegistrar is a
 * class that configures the plugin and registers the extensions that the plugin provides. The
 * CompilerPluginRegistrar is able to access any arguments provided to the plugin, through the
 * CompilerConfiguration object.
 *
 * This particular plugin uses AutoService to automatically register the plugin. If you are not using
 * AutoService, you will need to manually register the plugin in the META-INF/services directory.
 */
@Suppress("Unused")
@OptIn(ExperimentalCompilerApi::class)
@AutoService(CompilerPluginRegistrar::class)
class ExampleComponentRegistrar : CompilerPluginRegistrar() {

    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        val messageCollector =
            configuration.get(CommonConfigurationKeys.MESSAGE_COLLECTOR_KEY, MessageCollector.NONE)
        val usesK2 = configuration.languageVersionSettings.languageVersion.usesK2
        if (!usesK2) {
            messageCollector.report(
                severity = CompilerMessageSeverity.ERROR,
                message = "This plugin is only supported for the K2 compiler",
            )
            return
        }

        // The FirRegistrarAdapter is used to register extensions for the Fir compiler.
        // The Fir compiler is only available in the K2 compiler, which is why we check if the
        // compiler is using K2 before registering the extension.
        FirExtensionRegistrarAdapter.registerExtension(
            object : FirExtensionRegistrar() {

                // The configurePlugin function is where the plugin registers its extensions.
                // In this case, the plugin registers a the ExampleTransformer extension.
                // NOTE: Look carefully, you might miss the "+" operator in front of the Factory
                // block, which is the unaryPlus operator that registers the extension; it's a
                // slightly strange way of registering things, but that's how it's done.
                override fun ExtensionRegistrarContext.configurePlugin() {
                    +Factory { session ->
                        ExampleTransformer(session)
                    }
                }
            }
        )
    }
}