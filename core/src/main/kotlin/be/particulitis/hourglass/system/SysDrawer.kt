package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen.Companion.batch
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.comp.CompDir
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.DrawStyle.DIR_TRAIL
import be.particulitis.hourglass.comp.DrawStyle.NONE
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Vector2
import kotlin.math.sqrt


@Wire(failOnNull = false)
class SysDrawer : BaseEntitySystem(Aspect.all(CompSpace::class.java, CompDraw::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mDraw: ComponentMapper<CompDraw>
    private lateinit var mDir: ComponentMapper<CompDir>

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
                    process(it)
                }
        batch.end()
    }

    fun process(entityId: Int) {
        val draw = mDraw[entityId]
        val space = mSpace[entityId]
        batch.draw(space, draw)
        when (draw.drawingStyle) {
            DIR_TRAIL -> drawTrail(entityId, draw, space)
            NONE -> {
            }
        }

    }


    private val dirDisplay = Vector2()
    private fun drawTrail(entityId: Int, draw: CompDraw, space: CompSpace) {
        val dir = mDir[entityId]
        dirDisplay.set(dir.dir)
        dirDisplay.nor().scl(sqrt(space.w.toDouble()).toFloat()).scl(0.8f)
        if (dir != null) {
            for (i in 2..4) {
                val w = (space.w / (i + 3)) * 3f
                val x = (space.x + (space.w - w) / 2f) - dirDisplay.x * i
                val y = (space.y + (space.w - w) / 2f) - dirDisplay.y * i
                batch.draw(draw.color, x, y, w)
            }
        }
    }

}