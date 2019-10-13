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
import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt

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


    val dirDisplay = Vector2()
    private fun drawTrail(entityId: Int, draw: CompDraw, space: CompSpace) {
        val dir = mDir[entityId]
        dirDisplay.set(dir.dir)
        dirDisplay.nor().scl(sqrt(space.w.toDouble()).toFloat()).scl(0.8f)
        if (dir != null) {
            for (i in 2..4) {
                val w = (space.w / (i + 3)) * 3f
                val x = (space.x + (space.w - w) / 2f) - dirDisplay.x * i
                val y = (space.y + (space.w - w) / 2f) - dirDisplay.y * i
                FirstScreen.batch.draw(draw.color, x, y, w)
            }
        }
    }

}