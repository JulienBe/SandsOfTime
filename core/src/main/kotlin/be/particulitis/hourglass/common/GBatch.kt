package be.particulitis.hourglass.common

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.math.roundToInt

class GBatch(private val img: ImgMan) : SpriteBatch(8191) {

    fun draw(space: CompSpace, draw: CompDraw) {
        draw(draw.color, GResolution.baseX + space.x, GResolution.baseY + space.y, space.w)
    }

    fun draw(color: GPalette, x: Float, y: Float, w: Float) {
        packedColor = color.scale[1]
        draw(img.square, x.roundToInt().toFloat(), y.roundToInt().toFloat(), w.roundToInt().toFloat(), w.roundToInt().toFloat())
    }

}