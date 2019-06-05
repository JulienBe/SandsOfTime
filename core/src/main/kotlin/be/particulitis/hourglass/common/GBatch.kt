package be.particulitis.hourglass.common

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.comp.CompPos
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import kotlin.math.roundToInt

class GBatch(private val img: ImgMan) : SpriteBatch(8191) {


    fun draw(tr: TextureRegion, x: Int, y: Int, w: Float, h: Float) {
        draw(tr, x.toFloat(), y.toFloat(), w, h)
    }

    fun draw(tr: TextureRegion, x: Float, y: Float, w: Float, h: Float, angle: Float) {
        draw(tr, x, y, w / 2f, h / 2f, w, h, 1f, 1f, angle)
    }

    fun setColor(fl: Float) {
        packedColor = fl
    }

    fun drawInt(tr: TextureRegion, x: Float, y: Float, w: Float, h: Float, angle: Float) {
        draw(tr, (x.roundToInt()).toFloat(), (y.roundToInt()).toFloat(), (w.roundToInt()).toFloat(), (h.roundToInt()).toFloat(), angle)
    }

    fun drawInt(tr: TextureRegion, x: Float, y: Float, w: Float, h: Float) {
        draw(tr, (x.roundToInt()).toFloat(), (y.roundToInt()).toFloat(), (w.roundToInt()).toFloat(), (h.roundToInt()).toFloat())
    }
    fun drawIntOffset(tr: TextureRegion, x: Float, y: Float, w: Float, h: Float, offset: Float) {
        draw(tr, (x.roundToInt()).toFloat(), (y.roundToInt()).toFloat(), (w.roundToInt()).toFloat() + offset, (h.roundToInt()).toFloat() + offset)
    }
    fun drawIntOffset(tr: TextureRegion, x: Float, y: Float, w: Float, h: Float, offsetX: Float, offsetY: Float) {
        draw(tr, (x.roundToInt()).toFloat(), (y.roundToInt()).toFloat(), (w.roundToInt()).toFloat() + offsetX, (h.roundToInt()).toFloat() + offsetY)
    }

    fun draw(x: Float, y: Float, w: Float) {
        draw(img.square, x, y, w, w)
    }

    fun draw(pos: CompPos) {
        draw(img.square, pos.x, pos.y, pos.w, pos.h)
    }
}