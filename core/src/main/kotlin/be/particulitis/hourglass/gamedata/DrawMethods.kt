package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.common.GBatch
import be.particulitis.hourglass.comp.CompDir
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
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
    fun drawPlayer(space: CompSpace, draw: CompDraw, anim: Anims, baseColor: Int, batch: GBatch) {
        val frame = anim.frames[draw.cpt % anim.size]
        frame.forEachIndexed { index, color ->
            println("color = ${MathUtils.clamp(baseColor + color, 0, 3)}")
            batch.draw(
                    draw.color.scale[MathUtils.clamp(baseColor + color, 0, 3)],
                    space.x + Dim.player.third * (index % 3),
                    space.y + Dim.player.third * (index / 3),
                    Dim.player.third
            )
        }
    }
}