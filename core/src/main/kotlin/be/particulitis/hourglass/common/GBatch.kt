package be.particulitis.hourglass.common

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.comp.CompDimension
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class GBatch(private val img: ImgMan) : SpriteBatch(8191) {


    fun draw(dimension: CompDimension) {
        draw(img.square, GResolution.baseX + dimension.x, GResolution.baseY + dimension.y, dimension.w, dimension.h)
    }

    fun draw(color: Color, x: Float, y: Float, w: Float, h: Float) {
        packedColor = color.toFloatBits()
        draw(img.square, GResolution.baseX + x, GResolution.baseY + y, w, h)
    }
}