package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompButton
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.utils.IntBag
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import kotlin.math.abs

@Wire(failOnNull = false)
class SysUiControl : BaseEntitySystem(Aspect.all(CompSpace::class.java, CompButton::class.java)) {

    private lateinit var mButton: ComponentMapper<CompButton>
    private var selectedIndex = 0

    override fun processSystem() {
        GGraphics.batch.shader = null
        val entities = entityIds
        if (entities.size() == 0)
            return
        DrawerTools.draw {
            GGraphics.batch.setColor(1f, 1f, 1f, 1f)
            for (i in 0 until entities.size()) {
                val entityId = entities[i]
                val button = mButton[entityId]
                button.selected = false
            }
            val selectedButton = getSelected(entities)
            selectedButton.selected = true
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                selectedButton.onClick.invoke()
            }
            if (selectedIndex < entities.size() - 1 && (Gdx.input.isKeyJustPressed(Input.Keys.TAB) && !Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.DOWN))) {
                selectedIndex++
                bump(getSelected(entities))
            }
            if (selectedIndex > 0 && (Gdx.input.isKeyJustPressed(Input.Keys.TAB) && Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.UP))) {
                selectedIndex--
                bump(getSelected(entities))
            }
        }
    }

    private fun getSelected(entities: IntBag): CompButton {
        val selectedButtonId = entities[abs(selectedIndex) % entities.size()]
        return mButton[selectedButtonId]
    }

    private fun bump(button: CompButton) {
        button.outline.forEach {
            it.x.add(it.x.get() + GRand.gauss(1f))
            it.y.add(it.y.get() + GRand.gauss(1f))
        }
    }

}