package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color

@Wire(failOnNull = false)
class SysDrawer : IteratingSystem(Aspect.all(CompSpace::class.java, CompDraw::class.java)) {
    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mDraw: ComponentMapper<CompDraw>

    override fun process(entityId: Int) {
        FirstScreen.batch.setColor(1f, 0f, 0f, 1f)
        FirstScreen.batch.draw(mSpace[entityId])
        FirstScreen.batch.color = Color.WHITE
    }

}