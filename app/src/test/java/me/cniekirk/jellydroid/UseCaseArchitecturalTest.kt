package me.cniekirk.jellydroid

import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameEndingWith
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class UseCaseArchitecturalTest {

    @Test
    fun `classes with 'UseCase' suffix should have single public method named 'invoke' and reside in domain usecase package`() {
        Konsist.scopeFromProject()
            .classes()
            .withNameEndingWith(USECASE_CLASS_NAME)
            .assertTrue { useCaseClass ->
                val hasSingleInvokeMethod = useCaseClass.hasFunction { function ->
                    function.name == INVOKE_FUNCTION && function.hasPublicOrDefaultModifier
                }

                val hasSinglePublicDeclaration = useCaseClass.numPublicOrDefaultDeclarations() == 1
                val isInDomainUseCasePackage = useCaseClass.resideInPackage(DOMAIN_USECASE_PACKAGE)

                hasSingleInvokeMethod && hasSinglePublicDeclaration && isInDomainUseCasePackage
            }
    }

    companion object {
        private const val DOMAIN_USECASE_PACKAGE = "..domain.usecase"
        private const val INVOKE_FUNCTION = "invoke"
        private const val USECASE_CLASS_NAME = "UseCase"
    }
}