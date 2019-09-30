package be.particulitis.hourglass.common

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class GBatch(private val img: ImgMan) : SpriteBatch(8191) {

    fun draw(space: CompSpace) {
        draw(img.square, GResolution.baseX + space.x, GResolution.baseY + space.y, space.w, space.h)
    }

    fun draw(color: Color, x: Float, y: Float, w: Float, h: Float) {
        packedColor = color.toFloatBits()
        draw(img.square, GResolution.baseX + x, GResolution.baseY + y, w, h)
    }

    fun draw(r: Float, g: Float, b: Float, x: Float, y: Float, w: Float) {
        setColor(r, g, b, 1f)
        draw(img.square, GResolution.baseX + x, GResolution.baseY + y, w, w)
    }
}