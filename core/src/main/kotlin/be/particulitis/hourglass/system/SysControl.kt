package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.comp.CompControl
import be.particulitis.hourglass.comp.CompAction
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysControl : IteratingSystem(Aspect.all(CompControl::class.java, CompAction::class.java)) {

    private lateinit var mAction: ComponentMapper<CompAction>
    private lateinit var mCharControl: ComponentMapper<CompControl>

    override fun process(entityId: Int) {
        val actions = mAction[entityId]
        mCharControl[entityId].actions
                .filter { binding -> binding.keys.any { GInput.isKeyPressed(it) } }
                .forEach { actions.add(it.action) }
    }

}