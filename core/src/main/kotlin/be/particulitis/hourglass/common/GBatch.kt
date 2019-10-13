package be.particulitis.hourglass.common

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.math.roundToInt

class GBatch(private val img: ImgMan) : SpriteBatch(8191) {

    fun draw(space: CompSpace, draw: CompDraw) {
        draw(draw.color, space.x, space.y, space.w)
    }

    fun draw(color: GPalette, x: Float, y: Float, w: Float) {
        packedColor = color.scale[1]
        val x = (GResolution.baseX + x).roundToInt().toFloat()
        val y = (GResolution.baseY + y).roundToInt().toFloat()
        val w = w.roundToInt().toFloat()
        draw(img.square, x, y, w, w)
    }

}