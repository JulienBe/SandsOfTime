package be.particulitis.hourglass.gamedata.graphics

import be.particulitis.hourglass.common.GBatch
import be.particulitis.hourglass.comp.CompDir
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.Dim
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt

object DrawMethods {
    fun basic(space: CompSpace, draw: CompDraw, batch: GBatch) {
        batch.draw(space, draw)
    }

    private val dirDisplay = Vector2()
    fun drawTrail(draw: CompDraw, space: CompSpace, dir: CompDir, batch: GBatch) {
        dirDisplay.set(dir.dir)
        dirDisplay.nor().scl(sqrt(space.w.toDouble()).toFloat()).scl(0.8f)
        for (i in 2..4) {
            val w = (space.w / (i + 3)) * 3f
            val x = (space.x + (space.w - w) / 2f) - dirDisplay.x * i
            val y = (space.y + (space.w - w) / 2f) - dirDisplay.y * i
            batch.draw(draw.color, x, y, w)
        }
    }

    /**
     * expecting a 3*3 frame
     */
    fun draw33animLoop(space: CompSpace, draw: CompDraw, anim: Anims33, baseColor: Int, dim: Dim, batch: GBatch) {
        drawFrame(anim.frames[draw.cpt % anim.size], batch, draw, baseColor, space, dim)
    }
    fun draw33animNoLoop(space: CompSpace, draw: CompDraw, anim: Anims33, baseColor: Int, dim: Dim, batch: GBatch) {
        val cpt = MathUtils.clamp(draw.cpt, 0, anim.size - 1)
        println("cpt $cpt")
        drawFrame(anim.frames[MathUtils.clamp(draw.cpt, 0, anim.size - 1)], batch, draw, baseColor, space, dim)
    }

    private fun drawFrame(frame: IntArray, batch: GBatch, draw: CompDraw, baseColor: Int, space: CompSpace, dim: Dim) {
        frame.forEachIndexed { index, color ->
            batch.draw(
                    draw.color.scale[MathUtils.clamp(baseColor + color, 0, 3)],
                    space.x + dim.third * (index % 3),
                    space.y + dim.third * (index / 3),
                    dim.third
            )
        }
    }
}