package be.particulitis.hourglass.gamedata.graphics

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.comp.CompDir
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.math.Vector2
import kotlin.math.roundToInt
import kotlin.math.sqrt

object DrawMethods {
    fun basic(space: CompSpace, draw: CompDraw, batch: GGraphics) {
        batch.draw(space, draw)
    }

    fun drawNor(space: CompSpace, draw: CompDraw, batch: GGraphics) {
        batch.draw(draw.normal, space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat())
    }
    fun drawFront(space: CompSpace, draw: CompDraw, batch: GGraphics) {
        batch.draw(draw.texture, space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat())
    }

    private val dirDisplay = Vector2()
    fun drawTrail(draw: CompDraw, space: CompSpace, dir: CompDir, batch: GGraphics) {
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
    /**
    fun draw33animLoop(space: CompSpace, draw: CompDraw, anim: Anims33, baseColor: Int, dim: Dim, batch: GGraphics) {
        drawFrame(anim.frames[draw.cpt % anim.size], batch, draw, baseColor, space, dim)
    }
    fun draw33animNoLoop(space: CompSpace, draw: CompDraw, anim: Anims33, baseColor: Int, dim: Dim, batch: GGraphics) {
        drawFrame(anim.frames[MathUtils.clamp(draw.cpt, 0, anim.size - 1)], batch, draw, baseColor, space, dim)
    }
    */

    /**
    private fun drawFrame(frame: IntArray, batch: GGraphics, draw: CompDraw, baseColor: Int, space: CompSpace, dim: Dim) {
        frame.forEachIndexed { index, color ->
            batch.draw(
                    draw.color.scale[MathUtils.clamp(baseColor + color, 0, 3)],
                    space.x + dim.third * (index % 3),
                    space.y + dim.third * (index / 3),
                    dim.third
            )
        }
    }
    */
}