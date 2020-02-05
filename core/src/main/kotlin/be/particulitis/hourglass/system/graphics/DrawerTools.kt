package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GResolution
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer

object DrawerTools {
    fun drawToFb(buffer: FrameBuffer, xTrans: Float = 0f, yTrans: Float = 0f, drawFun: () -> Unit): Texture {
        GGraphics.cam.setToOrtho(false, GResolution.areaW, GResolution.areaH)
        GGraphics.cam.translate(xTrans, yTrans)
        GGraphics.cam.update()
        GGraphics.batch.projectionMatrix = GGraphics.cam.combined
        buffer.begin()
        buffer.bind()
        GGraphics.batch.begin()
        Gdx.graphics.gL20.glClearColor(0f, 0f, 0f, 0f)
        Gdx.graphics.gL20.glClear(GL20.GL_COLOR_BUFFER_BIT)
        drawFun.invoke()
        GGraphics.batch.end()
        buffer.end()
        val texture = buffer.colorBufferTexture
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        return texture
    }

    fun drawResult(vararg textures: Texture) {
        GGraphics.batch.shader = null
        GGraphics.batch.begin()
        GGraphics.cam.setToOrtho(false, GResolution.areaW, GResolution.areaH)
        GGraphics.cam.update()
        GGraphics.batch.projectionMatrix = GGraphics.cam.combined
        Gdx.gl20.glEnable(GL20.GL_BLEND)
        GGraphics.batch.setColor(1f, 1f, 1f, 1f)
        textures.forEach {
            GGraphics.batch.draw(it, 0f, GResolution.areaH, GResolution.areaW, -GResolution.areaH)
        }
    }

    fun frameBuffer(): FrameBuffer {
        return FrameBuffer(Pixmap.Format.RGBA8888, GResolution.areaW.toInt(), GResolution.areaH.toInt(), false)
    }
}