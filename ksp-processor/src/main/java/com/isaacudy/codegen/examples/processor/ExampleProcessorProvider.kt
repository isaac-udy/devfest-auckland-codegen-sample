package com.isaacudy.codegen.examples.processor

import com.google.auto.service.AutoService
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

/**
 * Every KSP processor needs a provider class that implements [SymbolProcessorProvider],
 * and creates a new instance of the processor. The [ExampleProcessorProvider] creates a
 * new [ExampleProcessor] instance, which is where the interesting logic of the processor is
 * defined.
 *
 * This particular processor uses AutoService to automatically register the SymbolProcessorProvider.
 * If you are not using AutoService, you will need to manually register the plugin in
 * the META-INF/services directory.
 */
@AutoService(SymbolProcessorProvider::class)
class ExampleProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return ExampleProcessor(
            environment = environment
        )
    }
}