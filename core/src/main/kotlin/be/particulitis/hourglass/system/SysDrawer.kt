package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GGraphics.Companion.batch
import be.particulitis.hourglass.common.drawing.GGraphics.Companion.cam
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.common.drawing.GShader
import be.particulitis.hourglass.comp.CompDraw
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import kotlin.reflect.KMutableProperty1

@Wire(failOnNull = false)
class SysDrawer : BaseEntitySystem(Aspect.all(CompDraw::class.java)) {

    private lateinit var mDraw: ComponentMapper<CompDraw>

    private val listEntitiesIds = mutableListOf<Int>()
    private val fboCurrent = FrameBuffer(Pixmap.Format.RGBA4444, GResolution.areaDim.toInt(), GResolution.areaDim.toInt(), false)
    private val fboNormal = FrameBuffer(Pixmap.Format.RGBA4444, GResolution.areaDim.toInt(), GResolution.areaDim.toInt(), false)
    private val fboLight = FrameBuffer(Pixmap.Format.RGBA4444, GResolution.areaDim.toInt(), GResolution.areaDim.toInt(), false)
    private val lightShader = GShader.createShader("shaders/light/vertex.glsl", "shaders/light/fragment.glsl")

    override fun processSystem() {
        batch.end()
        batch.shader = null
        val front = draw(GResolution.areaDim, GResolution.areaDim, true, fboCurrent) {
            actualDraw(CompDraw::texture)
        }
        val normal = draw(GResolution.areaDim, GResolution.areaDim, true, fboNormal) {
            actualDraw(CompDraw::normal)
        }
        batch.shader = lightShader
        val withLight = draw(GResolution.areaDim, GResolution.areaDim, true, fboLight) {
            setLights(lightShader)

            Gdx.graphics.gL20.glActiveTexture(GL20.GL_TEXTURE2)
            normal.bind()
            lightShader.setUniformi("u_normal", 2)

            Gdx.graphics.gL20.glActiveTexture(GL20.GL_TEXTURE1)
            GGraphics.imgMan.palette.bind()
            lightShader.setUniformi("u_palette", 1)

            Gdx.graphics.gL20.glActiveTexture(GL20.GL_TEXTURE0)
            front.bind()
            lightShader.setUniformi("u_texture", 0)

            batch.setColor(1f, 1f, 1f, 1f)
            batch.draw(front, 0f, GResolution.areaDim, GResolution.areaDim, -GResolution.areaDim)
        }

        batch.shader = null
        batch.begin()
        cam.setToOrtho(false, GResolution.screenWidth, GResolution.screenHeight)
        cam.update()
        batch.projectionMatrix = cam.combined
        Gdx.gl20.glEnable(GL20.GL_BLEND)
        batch.setColor(1f, 1f, 1f, 1f)
        batch.draw(withLight, GResolution.baseX, GResolution.areaDim, GResolution.areaDim, -GResolution.areaDim)
    }

    private fun draw(camW: Float, camH: Float, clean: Boolean, buffer: FrameBuffer, drawFun: () -> Unit): Texture {
        cam.setToOrtho(false, camW, camH)
        cam.update()
        batch.projectionMatrix = cam.combined
        buffer.begin()
        batch.begin()
        if (clean) {
            Gdx.graphics.gL20.glClearColor(0f, 0f, 0f, 0f)
            Gdx.graphics.gL20.glClear(GL20.GL_COLOR_BUFFER_BIT)
        }
        drawFun.invoke()
        batch.end()
        buffer.end()
        val texture = buffer.colorBufferTexture
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        return texture
    }

    private fun setLights(lightShader: ShaderProgram) {
        lightShader.setUniformi("u_light_count", GLight.numberOfLights())
        lightShader.setUniform1fv("u_light_intensity", GLight.intensity.values.toFloatArray(), 0, GLight.intensity.size)
        lightShader.setUniform2fv("u_light_pos", GLight.xy.values.toFloatArray(), 0, GLight.xy.size)
    }

    // TODO: in place sort
    private fun actualDraw(getTexture: KMutableProperty1<CompDraw, TextureRegion>) {
        val entities = entityIds
        listEntitiesIds.clear()
        for (i in 0 until entities.size()) {
            listEntitiesIds.add(entities[i])
        }
        listEntitiesIds
                .sortedBy {
                    mDraw[it].layer
                }
                .forEach {
                    mDraw[it].drawingStyle.invoke(batch, getTexture.invoke(mDraw[it]))
                }
    }

}