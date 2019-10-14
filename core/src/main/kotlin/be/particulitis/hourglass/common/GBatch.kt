package be.particulitis.hourglass.common

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import kotlin.math.roundToInt
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer



class GBatch(private val img: ImgMan) : SpriteBatch(8191) {

    fun drawAndSaveToMap(space: CompSpace, draw: CompDraw) {
        drawAndSaveToMap(draw.color, space.x, space.y, space.w)
    }

    fun drawAndSaveToMap(color: GPalette, x: Float, y: Float, w: Float) {
        packedColor = color.scale[1]
        val x = (GResolution.baseX + x).roundToInt().toFloat()
        val y = (GResolution.baseY + y).roundToInt().toFloat()
        val w = w.roundToInt().toFloat()

        val m_fbo = FrameBuffer(Pixmap.Format.RGB565, GResolution.areaDim as Int, GResolution.areaDim as Int, false)
        val m_fboRegion = TextureRegion(m_fbo.colorBufferTexture)
        m_fboRegion.flip(false, true)
        m_fbo.begin()

        draw(img.square, x, y, w, w)

        m_fbo.end()

    }

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