package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.comp.CompDir
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.DrawStyle.*
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem

@Wire(failOnNull = false)
class SysDrawer : IteratingSystem(Aspect.all(CompSpace::class.java, CompDraw::class.java)) {
    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mDraw: ComponentMapper<CompDraw>
    private lateinit var mDir: ComponentMapper<CompDir>

    override fun process(entityId: Int) {
        val draw = mDraw[entityId]
        val space = mSpace[entityId]
        FirstScreen.batch.draw(space, draw)
        when (draw.drawingStyle) {
            DIR_TRAIL -> drawTrail(entityId, draw, space)
            NONE -> {}
        }

    }

    private fun drawTrail(entityId: Int, draw: CompDraw, space: CompSpace) {
        val dir = mDir[entityId]
        if (dir != null) {
            for (i in 2..4) {
                val w = (space.w / (i + 1)) * 2f
                val x = (space.x + (space.w - w) / 2f) - (dir.x / 7f) * i
                val y = (space.y + (space.w - w) / 2f) - (dir.y / 7f) * i
                FirstScreen.batch.draw(draw.color, x, y, w)
            }
        }
    }

}