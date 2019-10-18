package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.FirstScreen.Companion.batch
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.comp.CompDraw
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer


@Wire(failOnNull = false)
class SysDrawer : BaseEntitySystem(Aspect.all(CompDraw::class.java)) {

    private lateinit var mDraw: ComponentMapper<CompDraw>

    private val listEntitiesIds = mutableListOf<Int>()
    private val fboShadow = FrameBuffer(Pixmap.Format.RGBA8888, GResolution.screenWidth.toInt(), GResolution.screenHeight.toInt(), false)
    private val fboCurrent = FrameBuffer(Pixmap.Format.RGBA8888, GResolution.screenWidth.toInt(), GResolution.screenHeight.toInt(), false)

    override fun processSystem() {
        batch.end()

        fboCurrent.begin()
        batch.begin()
        Gdx.graphics.gL20.glClearColor(0f, 0f, 0f, 0f)
        Gdx.graphics.gL20.glClear(GL20.GL_COLOR_BUFFER_BIT)
        actualDraw()
        fboCurrent.end()


        fboShadow.begin()
        batch.begin()
        batch.setColor(1f, 1f, 1f, 1f)
        val texture = drawFbo(fboCurrent)
        batch.end()
        fboShadow.end()


        batch.begin()
        batch.setColor(0.5f, 0.5f, 0.5f, 0.1f)
        drawFbo(fboShadow)
        batch.setColor(1f, 1f, 1f, 1f)
        batch.draw(texture, 0f, GResolution.screenHeight, GResolution.screenWidth, -GResolution.screenHeight)
    }

    private fun drawFbo(frameBuffer: FrameBuffer): Texture {
        val texture = frameBuffer.colorBufferTexture
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        batch.draw(texture, 0f, GResolution.screenHeight, GResolution.screenWidth, -GResolution.screenHeight)
        return texture
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
                    mDraw[it].drawingStyle.invoke(FirstScreen.batch)
                }
        batch.end()
    }

}