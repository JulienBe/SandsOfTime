package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompAction
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysClearActions : IteratingSystem(Aspect.all(CompAction::class.java)) {

    private lateinit var mAction: ComponentMapper<CompAction>

    override fun process(entityId: Int) {
        mAction[entityId].clear()
    }
}