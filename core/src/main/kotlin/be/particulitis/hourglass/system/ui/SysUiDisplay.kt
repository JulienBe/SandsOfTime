package be.particulitis.hourglass.system.ui

import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompButton
import be.particulitis.hourglass.comp.ui.CompTxt
import be.particulitis.hourglass.font.FontPixel
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx

@Wire(failOnNull = false)
class SysUiDisplay : IteratingSystem(Aspect.all(CompSpace::class.java).one(CompTxt::class.java, CompButton::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mTxt: ComponentMapper<CompTxt>
    private lateinit var mButton: ComponentMapper<CompButton>

    override fun process(entityId: Int) {
        val space = mSpace[entityId]
        val txt = if (mButton.has(entityId)) mButton[entityId] else mTxt[entityId]
        txt.pixels.forEach {
            it.act(GTime.delta)
            it.draw(GResolution.baseX + space.x, GResolution.baseY + space.y, 1)
            it.boost = false
        }
        if (Gdx.input.justTouched() &&
                txt is CompButton &&
                GHelper.isClicked(space.x, space.y, FontPixel.width(txt.text, 1), FontPixel.height(1))) {
            txt.onClick.invoke()
        }
    }

}