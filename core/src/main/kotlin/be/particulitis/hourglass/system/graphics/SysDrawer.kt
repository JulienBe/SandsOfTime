package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GGraphics.Companion.batch
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
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.MathUtils

@Wire(failOnNull = false)
class SysDrawer : BaseEntitySystem(Aspect.all(CompDraw::class.java)) {

    private lateinit var mDraw: ComponentMapper<CompDraw>

    private val listEntitiesIds = mutableListOf<Int>()
    private val fboCurrent = DrawerTools.frameBuffer()
    private val fboNormal = DrawerTools.frameBuffer()
    private val fboLight = DrawerTools.frameBuffer()
    private val lightShader = GShader.createShader("shaders/light/vertex.glsl", "shaders/light/fragment.glsl")
    private val normalShader = GShader.createShader("shaders/normal/vertex.glsl", "shaders/normal/fragment.glsl")
    private lateinit var mergedTexture: Texture

    override fun processSystem() {
        batch.end()
        batch.shader = null
        val sortedEntities = sortEntities()
        val front = DrawerTools.drawToFb(fboCurrent) {
            sortedEntities.forEach {
                mDraw[it].drawFront.invoke(batch)
            }
        }
        batch.shader = normalShader
        val normal = DrawerTools.drawToFb(fboNormal) {
            var previousAngle = -1546.54f
            sortedEntities.forEach {
                val draw = mDraw[it]
                var angle = draw.normalAngle * MathUtils.degreesToRadians
                if (previousAngle != angle) {
                    batch.flush()
                    normalShader.setUniformf("u_angle", angle)
                    previousAngle = angle
                }
                draw.drawNormal.invoke(batch)
            }
        }
        batch.shader = lightShader
        mergedTexture = DrawerTools.drawToFb(fboLight) {
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
//        mergedTexture = normal
        //DrawerTools.drawResult(mergedTexture)
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