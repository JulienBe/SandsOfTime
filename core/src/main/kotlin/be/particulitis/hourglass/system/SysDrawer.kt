package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompDimension
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem

@Wire(failOnNull = false)
class SysDrawer : IteratingSystem(Aspect.all(CompDimension::class.java, CompDraw::class.java)) {
    private lateinit var mDimension: ComponentMapper<CompDimension>
    private lateinit var mDraw: ComponentMapper<CompDraw>

    override fun process(entityId: Int) {
        mDraw[entityId].draw(mDimension[entityId], FirstScreen.batch)
    }

}