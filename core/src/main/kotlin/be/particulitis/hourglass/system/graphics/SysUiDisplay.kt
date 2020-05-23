package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GPalette
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
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sin

@Wire(failOnNull = false)
class SysUiDisplay : BaseEntitySystem(Aspect.all(CompSpace::class.java).one(CompTxt::class.java, CompButton::class.java, CompPrettyUi::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mTxt: ComponentMapper<CompTxt>
    private lateinit var mButton: ComponentMapper<CompButton>
    private lateinit var mPrettyUi: ComponentMapper<CompPrettyUi>
    private val leftSelected = FontPixel.get(0, '>', 2)
    private val rightSelected = FontPixel.get(0, '<', 2)

    override fun processSystem() {
        GGraphics.batch.shader = null
        DrawerTools.draw {
            GGraphics.batch.setColor(1f, 1f, 1f, 1f)
            val entities = entityIds
            for (i in 0 until entities.size()) {
                val entityId = entities[i]
                val space = mSpace[entityId]
                val txt = getTxt(entityId)
                txt.allFontPixels.forEach {
                    drawPixel(it, space)
                }
                if (mButton.has(entityId)) {
                    val button = mButton[entityId]
                    button.outline.forEach {
                        if (it.act(GTime.delta))
                            for (i in 0..FontPixel.trailSize)
                                GGraphics.batch.draw(it.tr, (space.x + it.x.get(i)).roundToInt().toFloat(), (space.y + it.y.get(i)).roundToInt().toFloat(), 1f, 1f)
                    }
                    if (button.selected)
                        displaySelectionOnButton(button, space)
                }
                if (Gdx.input.justTouched() && txt is CompButton && GHelper.isClicked(space.x, space.y, FontPixel.width(txt.text, 1), FontPixel.height(1)))
                    txt.onClick.invoke()
            }
        }
    }

    private fun drawPixel(it: FontPixel, space: CompSpace) {
        if (it.act(GTime.delta)) {
            for (i in 0..FontPixel.trailSize step 2)
                GGraphics.batch.draw(it.tr, (space.x + it.x.get(i)).roundToInt().toFloat(), (space.y + it.y.get(i)).roundToInt().toFloat(), 1f, 1f)
            it.boost = false
        }
    }

    private fun displaySelectionOnButton(button: CompButton, space: CompSpace) {
        leftSelected.forEach {
            if (it.act(GTime.delta))
                GGraphics.batch.draw(it.tr, (space.x + it.x.get() - 10).roundToInt().toFloat(), (space.y + it.y.get()).roundToInt().toFloat(), 1f, 1f)
        }
        rightSelected.forEach {
            if (it.act(GTime.delta))
                GGraphics.batch.draw(it.tr, (button.w + space.x + it.x.get() + 3).roundToInt().toFloat(), (space.y + it.y.get()).roundToInt().toFloat(), 1f, 1f)
        }
        for (i in 0..button.outline.size / 80) {
            for (i in 0..8 + ((sin(GTime.time * 2f)) * 6f).toInt()) {
                hightButton(button, space, button.selectedHighlightIndex + i)
                hightButton(button, space, ((((button.selectedHighlightIndex * 1.3f) + i) % button.outline.size) - button.outline.size).toInt())
            }
            button.selectedHighlightIndex++
        }
    }

    private fun hightButton(button: CompButton, space: CompSpace, index: Int) {
        val highlight = button.outline[abs(index % button.outline.size)]
        highlight.x.add(highlight.desiredX)
        highlight.y.add(highlight.desiredY)
        highlight.initDelay = -1f
        GGraphics.batch.draw(GPalette.values()[abs(index % GPalette.values().size)].tr, (space.x + highlight.x.get()).roundToInt().toFloat(), (space.y + highlight.y.get()).roundToInt().toFloat(), 1f, 1f)
    }

    private fun getTxt(entityId: Int): CompTxt {
        if (mButton.has(entityId)) return mButton[entityId]
        if (mTxt.has(entityId)) return mTxt[entityId]
        return mPrettyUi[entityId]
    }

}