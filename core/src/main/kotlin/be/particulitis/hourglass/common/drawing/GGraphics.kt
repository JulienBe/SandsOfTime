package be.particulitis.hourglass.common.drawing

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion

class GGraphics(private val img: ImgMan) : SpriteBatch(8191) {

    fun draw(space: CompSpace, draw: CompDraw) {
        draw(draw.color, space.x, space.y, space.w)
    }

    fun draw(color: GPalette, x: Float, y: Float, w: Float) {
        //packedColor = color.f
        draw(img.square, x, y, w, w)
    }

    fun draw(color: Float, x: Float, y: Float, w: Float) {
        packedColor = color
        draw(img.square, x, y, w, w)
    }

    fun draw(color: Float, x: Int, y: Int, w: Int) {
        packedColor = color
        draw(img.square, x.toFloat(), y.toFloat(), w.toFloat(), w.toFloat())
    }

    fun drawWhite(texture: TextureRegion, x: Float, y: Float) {
        packedColor = Color.WHITE_FLOAT_BITS
        draw(texture, x, y)
    }

    companion object {

        fun render(function: () -> Unit) {
            Gdx.graphics.gL20.glClearColor(0f, 0f, 0f, 0f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
            cam.update()
            batch.projectionMatrix = cam.combined
            batch.begin()
            function.invoke()
            batch.end()
        }

        fun nor(s: String): TextureRegion {
            return imgMan.nor(s)
        }
        fun tr(s: String): TextureRegion {
            println("get $s")
            return imgMan.tr(s)
        }

        val batch: GGraphics
        val imgMan = ImgMan()
        val cam = OrthographicCamera(GResolution.screenWidth, GResolution.screenHeight)

        init {
            batch =  GGraphics(imgMan)
        }
    }

}