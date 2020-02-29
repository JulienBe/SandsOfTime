package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompAct
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem

@Wire(failOnNull = false)
class SysAct : IteratingSystem(Aspect.all(CompAct::class.java)) {

    private lateinit var mAct: ComponentMapper<CompAct>

    override fun process(entityId: Int) {
        mAct[entityId].act.invoke()
    }

}