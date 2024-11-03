package com.isaacudy.codegen.examples.plugin

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirDeclaration
import org.jetbrains.kotlin.fir.declarations.FirDeclarationStatus
import org.jetbrains.kotlin.fir.declarations.FirFunction
import org.jetbrains.kotlin.fir.declarations.FirValueParameter
import org.jetbrains.kotlin.fir.declarations.utils.nameOrSpecialName
import org.jetbrains.kotlin.fir.expressions.FirExpression
import org.jetbrains.kotlin.fir.expressions.FirStatement
import org.jetbrains.kotlin.fir.expressions.buildResolvedArgumentList
import org.jetbrains.kotlin.fir.expressions.builder.buildBlock
import org.jetbrains.kotlin.fir.expressions.builder.buildFunctionCall
import org.jetbrains.kotlin.fir.expressions.builder.buildLiteralExpression
import org.jetbrains.kotlin.fir.extensions.FirStatusTransformerExtension
import org.jetbrains.kotlin.fir.references.builder.buildResolvedNamedReference
import org.jetbrains.kotlin.fir.resolve.fqName
import org.jetbrains.kotlin.fir.resolve.providers.FirSymbolProvider
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.SymbolInternals
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.ConstantValueKind

/**
 * This is an example FirStatusTransformerExtension that adds a call to "ScreenLifecycleEffect" to
 * all top-level @Composable functions that have a name ending with "Screen". This is registered
 * in the ExampleComponentRegistrar.
 */
class ExampleTransformer(
    session: FirSession,
) : FirStatusTransformerExtension(session) {

    /**
     * needTransformStatus is a function that determines whether a given declaration should be
     * transformed by this extension. In this case, we only want to transform top-level functions
     * that have a name ending with "Screen" and are annotated with @Composable, so we return true
     * for those declarations.
     */
    override fun needTransformStatus(declaration: FirDeclaration): Boolean {
        return declaration is FirFunction
                && declaration.hasComposableAnnotation(session)
                && declaration.nameOrSpecialName.asString().endsWith("Screen")
    }

    /**
     * transformStatus is a function that transforms a declaration. In this case, we
     * add a call to "ScreenLifecycleEffect" to the beginning of the body of the function.
     */
    override fun transformStatus(
        status: FirDeclarationStatus,
        declaration: FirDeclaration
    ): FirDeclarationStatus {
        // needTransformStatus should ensure that declaration is a FirFunction
        if (declaration !is FirFunction) return super.transformStatus(status, declaration)

        // Replace the body of the declaration with a new body that adds a call to
        // "ScreenLifecycleEffect" as the first statement in the body, and then adds
        // the original body statements.
        declaration.replaceBody(
            newBody = buildBlock {
                // IMPORTANT!
                // This is where we add the call to "ScreenLifecycleEffect"
                statements.add(
                    createScreenLifecycleEffectStatement(declaration)
                )
                // Add the original body statements (if any)
                declaration.body?.let { body ->
                    statements.addAll(body.statements)
                }
            }
        )
        return super.transformStatus(status, declaration)
    }

    /**
     * createScreenLifecycleEffectStatement is the key function of ExampleTransformer. It creates
     * a FirStatement that calls the "ScreenLifecycleEffect" function with the name of the function
     * that is being transformed.
     */
    @OptIn(SymbolInternals::class)
    private fun createScreenLifecycleEffectStatement(
        function: FirFunction,
    ): FirStatement {
        // Get a reference to the "ScreenLifecycleEffect" function
        val funScreenLifecycleEffect = session.symbolProvider.funScreenLifecycleEffect
        val functionName = function.nameOrSpecialName.asString()
        return buildFunctionCall {
            calleeReference = buildResolvedNamedReference {
                this.name = funScreenLifecycleEffect.name
                this.source = function.source
                this.resolvedSymbol = funScreenLifecycleEffect
            }
            // Pass the name of the function as the argument to "ScreenLifecycleEffect"
            argumentList = buildResolvedArgumentList(
                original = null,
                mapping = LinkedHashMap<FirExpression, FirValueParameter>().apply {
                    put(
                        buildStringLiteralExpression(functionName),
                        funScreenLifecycleEffect.valueParameterSymbols.first().fir
                    )
                },
            )
        }
    }
}

/**
 * This is a helper function for checking if a FirDeclaration has the @Composable annotation.
 */
private fun FirDeclaration.hasComposableAnnotation(session: FirSession): Boolean {
    return annotations.any {
        it.fqName(session)?.asString() == "androidx.compose.runtime.Composable"
    }
}

/**
 * This is a helper function for creating a simple string literal expression.
 */
private fun buildStringLiteralExpression(
    value: String,
): FirExpression {
    return buildLiteralExpression(
        source = null,
        kind = ConstantValueKind.String,
        value = value,
        setType = true
    )
}

/**
 * This is a helper function/getter for getting the FirNamedFunctionSymbol for the
 * "ScreenLifecycleEffect" function.
 */
private val FirSymbolProvider.funScreenLifecycleEffect: FirNamedFunctionSymbol
    get() = getTopLevelFunctionSymbols(
        FqName("com.isaacudy.codegen.example.plugin"),
        Name.identifier("ScreenLifecycleEffect"),
    ).single()
