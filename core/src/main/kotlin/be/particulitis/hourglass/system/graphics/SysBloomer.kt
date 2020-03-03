package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.drawing.GGraphics.Companion.batch
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.CompBloomer
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.forEach
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.graphics.Pixmap
import com.crashinvaders.vfx.VfxManager
import com.crashinvaders.vfx.effects.BloomEffect
import com.crashinvaders.vfx.framebuffer.VfxFrameBuffer

@Wire(failOnNull = false)
class SysBloomer : BaseEntitySystem(Aspect.all(CompBloomer::class.java)) {

    private lateinit var mBloomer: ComponentMapper<CompBloomer>
    private lateinit var mSpace: ComponentMapper<CompSpace>

    private val vfxManager = VfxManager(Pixmap.Format.RGBA8888)
    private val vfxBloom = BloomEffect(Pixmap.Format.RGBA8888)
    private val vfxFbo = VfxFrameBuffer(Pixmap.Format.RGBA8888)

    init {
        vfxBloom.baseIntensity = 1f
        vfxBloom.bloomIntensity = 2.5f
        vfxBloom.baseSaturation = 0.5f
        vfxBloom.bloomSaturation = 2f
        vfxBloom.blurAmount = 1.5f
        vfxManager.addEffect(vfxBloom)
        vfxManager.resize(GResolution.areaW.toInt(), GResolution.areaH.toInt())
        vfxFbo.initialize(GResolution.areaW.toInt(), GResolution.areaH.toInt())
    }

    override fun processSystem() {
        batch.shader = null
        vfxManager.cleanUpBuffers()
        vfxManager.beginCapture()
        DrawerTools.draw {
            entityIds.forEach {
                val bloomer = mBloomer[it]
                bloomer.preDraw.invoke()
                bloomer.draw.invoke(batch, mSpace[it])
            }
        }
        vfxManager.endCapture()
        vfxManager.applyEffects()
        vfxManager.renderToFbo(vfxFbo)
        DrawerTools.drawResult(vfxFbo.fbo.colorBufferTexture)
    }

}