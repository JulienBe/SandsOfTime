package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.GAction
import com.artemis.PooledComponent

class CompAction : PooledComponent() {
    val actions = mutableListOf<GAction>()

    fun add(action: GAction) {
        actions.add(action)
    }

    override fun reset() {
        actions.clear()
    }

    fun clear() {
        reset()
    }
}