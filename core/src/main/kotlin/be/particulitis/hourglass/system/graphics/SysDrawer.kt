package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GGraphics.Companion.batch
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.common.drawing.GShader
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.MathUtils

@Wire(failOnNull = false)
class SysDrawer : BaseEntitySystem(Aspect.all(CompDraw::class.java)) {

    private lateinit var mDraw: ComponentMapper<CompDraw>
    private lateinit var mSpace: ComponentMapper<CompSpace>

    private val listEntitiesIds = mutableListOf<Int>()
    private val fboCurrent = DrawerTools.frameBuffer()
    private val fboOccluders = DrawerTools.frameBuffer()
    private val fboNormal = DrawerTools.frameBuffer()
    private val fboLight = DrawerTools.frameBuffer()
    private val lightShader = GShader.createShader("shaders/light/vertex.glsl", "shaders/light/fragment.glsl")
    private val normalShader = GShader.createShader("shaders/normal/vertex.glsl", "shaders/normal/fragment.glsl")


    private lateinit var mergedTexture: Texture


    override fun processSystem() {
        val sortedEntities = sortEntities()
        batch.end()

        // I don't know if it's worth filtering what really is an occluder or not. We'll see when it drops below 40fps on my t420
        val occluders = DrawerTools.drawToFb(fboOccluders) {
            sortedEntities.forEach {
                mDraw[it].preDraw.invoke()
                batch.drawOccCenteredOnBox(mDraw[it], mSpace[it])
            }
        }

        val front = DrawerTools.drawToFb(fboCurrent) {
            sortedEntities.forEach {
                val draw = mDraw[it]
                draw.drawFront.invoke(batch, draw, mSpace[it])
            }
        }
        batch.shader = normalShader
        val normal = DrawerTools.drawToFb(fboNormal) {
            sortedEntities.forEach {
                val draw = mDraw[it]
                normalShader.setUniformf("u_angle", draw.angle * MathUtils.degreesToRadians)
                draw.drawNormal.invoke(batch, draw, mSpace[it])
            }
        }
        batch.shader = lightShader
        mergedTexture = DrawerTools.drawToFb(fboLight) {
            setLights(lightShader)

            Gdx.graphics.gL20.glActiveTexture(GL20.GL_TEXTURE3)
            occluders.bind()
            lightShader.setUniformi("u_occlusion", 3)

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
    }

    private fun sortEntities(): List<Int> {
        val entities = entityIds
        listEntitiesIds.clear()
        for (i in 0 until entities.size()) {
            listEntitiesIds.add(entities[i])
        }
        return listEntitiesIds.sortedBy {
            mDraw[it].layer
        }
    }

    private fun setLights(lightShader: ShaderProgram) {
        lightShader.setUniformi("u_light_count", GLight.numberOfLights())
        lightShader.setUniform1fv("u_light_intensity", GLight.intensity.values.toFloatArray(), 0, GLight.intensity.size)
        lightShader.setUniform4fv("u_light_pos_angle_tilt", GLight.xyat.values.toFloatArray(), 0, GLight.xyat.size)
    }

    fun getCurrentTexture(): Texture {
        return mergedTexture
    }

}