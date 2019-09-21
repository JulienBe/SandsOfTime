package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.builds.Aspects
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.CompTxt
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem

@Wire(failOnNull = false)
class SysUiDisplay : IteratingSystem(Aspect.all(Aspects.Ui.comps)) {
    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mDraw: ComponentMapper<CompDraw>
    private lateinit var mTxt: ComponentMapper<CompTxt>

    override fun process(entityId: Int) {
        val draw = mDraw[entityId]
        val space= mSpace[entityId]
    }

}