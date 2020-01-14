package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.common.drawing.GShader
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompButton
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.comp.ui.CompTxt
import be.particulitis.hourglass.font.FontPixel
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import kotlin.math.roundToInt

@Wire(failOnNull = false)
class SysUiDisplay : BaseEntitySystem(Aspect.all(CompSpace::class.java).one(CompTxt::class.java, CompButton::class.java, CompPrettyUi::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mTxt: ComponentMapper<CompTxt>
    private lateinit var mButton: ComponentMapper<CompButton>
    private lateinit var mPrettyUi: ComponentMapper<CompPrettyUi>
    private val shader = GShader.createShader("shaders/font/vertex.glsl", "shaders/font/fragment.glsl")
    private val fboCurrent = DrawerTools.frameBuffer()

    override fun processSystem() {
        GGraphics.batch.shader = shader


        val backgroundTexture = world.getSystem(SysDrawer::class.java).getCurrentTexture()

        val mergedTexture = DrawerTools.drawToFb(fboCurrent) {

            Gdx.graphics.gL20.glActiveTexture(GL20.GL_TEXTURE1)
            backgroundTexture.bind()
            shader.setUniformi("u_background_texture", 1)

            Gdx.graphics.gL20.glActiveTexture(GL20.GL_TEXTURE0)
            GGraphics.imgMan.palette.bind()
            shader.setUniformi("u_palette", 0)

            GGraphics.batch.setColor(1f, 1f, 1f, 1f)
            val entities = entityIds
            for (i in 0 until entities.size()) {
                val entityId = entities[i]
                val space = mSpace[entityId]
                val txt = getTxt(entityId)
                txt.pixels.forEach {
                    it.act(GTime.delta)
                    GGraphics.batch.draw(it.tr, (space.x + it.x).roundToInt().toFloat(), (space.y + it.y).roundToInt().toFloat(), FontPixel.fontWidth, FontPixel.fontWidth)
                    it.boost = false
                }
                if (Gdx.input.justTouched() && txt is CompButton &&
                        GHelper.isClicked(space.x, space.y, FontPixel.width(txt.text, 1), FontPixel.height(1))) {
                    txt.onClick.invoke()
                }
            }
        }


        DrawerTools.drawResult(backgroundTexture, mergedTexture)
    }

    private fun getTxt(entityId: Int): CompTxt {
        if (mButton.has(entityId)) return mButton[entityId]
        if (mTxt.has(entityId)) return mTxt[entityId]
        return mPrettyUi[entityId]
    }

}