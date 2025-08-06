package me.cniekirk.jellydroid

import androidx.lifecycle.ViewModel
import com.lemonappdev.konsist.api.KoModifier
import com.lemonappdev.konsist.api.Konsist
import com.lemonappdev.konsist.api.ext.list.withNameContaining
import com.lemonappdev.konsist.api.ext.list.withParentOf
import com.lemonappdev.konsist.api.verify.assertTrue
import org.junit.Test

class ViewModelArchitecturalTest {

    @Test
    fun `classes extending 'ViewModel' should have 'ViewModel' suffix`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withParentOf(ViewModel::class)
            .assertTrue { it.name.endsWith(VIEW_MODEL_CLASS_NAME) }
    }

    @Test
    fun `Every 'ViewModel' extends ContainerHost`() {
        Konsist.scopeFromProject()
            .classes()
            .withParentOf(ViewModel::class)
            .assertTrue { viewModelClass ->
                // Every ViewModel needs to have a parent class that contains ContainerHost in it's name
                viewModelClass.parents().withNameContaining(CONTAINER_HOST_CLASS_NAME).isNotEmpty()
            }
    }

    @Test
    fun `classes extending 'ViewModel' should have internal modifier`() {
        Konsist
            .scopeFromProject()
            .classes()
            .withParentOf(ViewModel::class)
            .assertTrue { it.modifiers.contains(KoModifier.INTERNAL) }
    }

    companion object {
        private const val VIEW_MODEL_CLASS_NAME = "ViewModel"
        private const val CONTAINER_HOST_CLASS_NAME = "ContainerHost"
    }
}