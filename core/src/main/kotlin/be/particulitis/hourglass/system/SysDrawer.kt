package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompPos
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem

@Wire(failOnNull = false)
class SysDrawer : IteratingSystem(Aspect.all(CompPos::class.java, CompDraw::class.java)) {
    private lateinit var mPos: ComponentMapper<CompPos>
    private lateinit var mDraw: ComponentMapper<CompDraw>

    override fun process(entityId: Int) {
        mDraw[entityId].draw(mPos[entityId], FirstScreen.batch)
    }

}