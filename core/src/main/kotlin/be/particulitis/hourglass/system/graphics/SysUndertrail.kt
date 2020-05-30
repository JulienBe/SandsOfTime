package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.drawing.GGraphics.Companion.batch
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.draw.CompUndertrail
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion

@Wire(failOnNull = false)
class SysUndertrail : BaseEntitySystem(Aspect.all(CompUndertrail::class.java)) {

    private lateinit var mTrail: ComponentMapper<CompUndertrail>
    private lateinit var mSpace: ComponentMapper<CompSpace>

    private val fboCurrent = DrawerTools.frameBuffer()
    private var previousFrame: Texture? = null
    private var currentFrame: Texture? = null

    override fun processSystem() {
        batch.end()
        batch.shader = null

        previousFrame = currentFrame
        currentFrame = DrawerTools.drawToFb(fboCurrent, 0f, 0f, false) {
            val entities = entityIds
            if (previousFrame != null)
                batch.draw(TextureRegion(previousFrame), 0f, GResolution.areaH, GResolution.areaW, -GResolution.areaH)
            for (i in 0 until entities.size()) {
                val id = entities[i]
                mTrail[id].draw.invoke(batch, mSpace[id])
            }
        }
        finalTexture = currentFrame!!
    }

    companion object {
        var finalTexture: Texture = GPalette.PINK.tr.texture
        var angle = 0f
    }

}