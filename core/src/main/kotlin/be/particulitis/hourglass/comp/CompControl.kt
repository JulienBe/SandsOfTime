package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.GAction
import be.particulitis.hourglass.common.GActionBinding
import com.artemis.PooledComponent

class CompControl : Comp() {

    val actions = mutableListOf<GActionBinding>()

    override fun reset() {
        actions.clear()
    }

    fun addAction(keys: List<Int>, action: GAction) {
        actions.add(GActionBinding(keys, action))
    }
}