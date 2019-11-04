package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen.Companion.batch
import be.particulitis.hourglass.FirstScreen.Companion.cam
import be.particulitis.hourglass.FirstScreen.Companion.lightShader
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.comp.CompDraw
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.graphics.glutils.ShaderProgram

@Wire(failOnNull = false)
class SysDrawer : BaseEntitySystem(Aspect.all(CompDraw::class.java)) {

    private lateinit var mDraw: ComponentMapper<CompDraw>

    private val listEntitiesIds = mutableListOf<Int>()
    private val fboShadow = FrameBuffer(Pixmap.Format.RGBA4444, GResolution.areaDim.toInt(), GResolution.areaDim.toInt(), false)
    private val fboCurrent = FrameBuffer(Pixmap.Format.RGBA4444, GResolution.areaDim.toInt(), GResolution.areaDim.toInt(), false)
    private val fboLight = FrameBuffer(Pixmap.Format.RGBA4444, GResolution.areaDim.toInt(), GResolution.areaDim.toInt(), false)

    override fun processSystem() {

        if (Gdx.input.justTouched())
            GLight.create(GHelper.x, GHelper.y, GRand.nextFloat(), GRand.nextFloat(), GRand.nextFloat())
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            GLight.clear()

        batch.end()

        batch.shader = lightShader
        val lightTexture = draw(GResolution.areaDim, GResolution.areaDim, true, fboLight) {
            setLights(lightShader)
            batch.draw(Color.WHITE_FLOAT_BITS, 0f, 0f, GResolution.areaDim)
        }
        batch.shader = null

        val front = draw(GResolution.areaDim, GResolution.areaDim, true, fboCurrent) {
            actualDraw()
        }

        val shadow = draw(GResolution.areaDim, GResolution.areaDim, false, fboShadow) {
            batch.setColor(1f, 1f, 1f, 1f)
            batch.draw(front, 0f, GResolution.screenHeight, GResolution.areaDim, -GResolution.screenHeight)
        }

        batch.begin()
        cam.setToOrtho(false, GResolution.screenWidth, GResolution.screenHeight)
        cam.update()
        batch.projectionMatrix = cam.combined
        Gdx.gl20.glEnable(GL20.GL_BLEND)
        batch.color = Color.WHITE
        batch.setColor(0.5f, 0.5f, 0.5f, 0.1f)
        batch.draw(shadow, GResolution.baseX, 0f, GResolution.areaDim, GResolution.areaDim)
        batch.setColor(1f, 1f, 1f, 1f)
        batch.draw(front, GResolution.baseX, GResolution.screenHeight, GResolution.areaDim, -GResolution.screenHeight)
        batch.draw(lightTexture, GResolution.baseX, GResolution.screenHeight, GResolution.areaDim, -GResolution.screenHeight)
    }

    private fun draw(camW: Float, camH: Float, clean: Boolean, buffer: FrameBuffer, drawFun: () -> Unit): Texture {
        cam.setToOrtho(true, camW, camH)
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
        lightShader.setUniform2fv("u_light_pos", GLight.xy, 0, GLight.numberOfLights() * 2)
        lightShader.setUniform3fv("u_light_color", GLight.rgb, 0, GLight.numberOfLights() * 3)
    }

    // TODO: in place sort
    private fun actualDraw() {
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
                    mDraw[it].drawingStyle.invoke(batch)
                }
    }

}