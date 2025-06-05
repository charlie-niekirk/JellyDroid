package me.cniekirk.jellydroid.core.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import me.cniekirk.jellydroid.core.navigation.routes.TabRoute
import me.cniekirk.jellydroid.core.navigation.routes.TopLevelRoute

class AppBackStack<T : Any>(startKey: T) {

    // Maintain a stack for each top level route
    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf()

    // Expose the current top level route for consumers
    var topLevelKey by mutableStateOf(startKey)
        private set

    // Expose the back stack so it can be rendered by the NavDisplay
    val backStack = mutableStateListOf(startKey)

    var isTopLevelOrTabRoute by mutableStateOf(false)
        private set

    private fun updateBackStack() =
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
            updateIsTopLevelOrTabRouteVisible()
        }

    fun addTopLevel(key: T) {
        // If the top level doesn't exist, add it
        if (topLevelStacks[key] == null) {
            topLevelStacks[key] = mutableStateListOf(key)
        } else {
            // Otherwise just move it to the end of the stacks
            topLevelStacks.apply {
                remove(key)?.let {
                    put(key, it)
                }
            }
        }
        topLevelKey = key
        updateBackStack()
    }

    fun add(key: T) {
        // Cannot just add top level route
        if (key is TopLevelRoute) return

        if (key is TabRoute) {
            topLevelStacks[topLevelKey]?.add(key)
            updateBackStack()
        } else {
            backStack.add(key)
            updateIsTopLevelOrTabRouteVisible()
        }
    }

    fun removeLast() {
        val isLastPartOfTopLevel = backStack.lastOrNull() == topLevelStacks[topLevelKey]?.lastOrNull()

        if (isLastPartOfTopLevel) {
            val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
            // If the removed key was a top level key, remove the associated top level stack
            topLevelStacks.remove(removedKey)
            topLevelKey = topLevelStacks.keys.last()
            updateBackStack()
        } else {
            backStack.removeLastOrNull()
            updateIsTopLevelOrTabRouteVisible()
        }
    }

    @Suppress("Indentation")
    private fun updateIsTopLevelOrTabRouteVisible() {
        isTopLevelOrTabRoute = backStack
            .any { it is TopLevelRoute } &&
                (backStack.lastOrNull() is TopLevelRoute || backStack.lastOrNull() is TabRoute)
    }
}
