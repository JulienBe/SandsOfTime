package be.particulitis.hourglass.system.ui

import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.font.FontAnim
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

@Wire(failOnNull = false)
class SysUiPrettyDisplay : IteratingSystem(Aspect.all(CompSpace::class.java, CompPrettyUi::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mUi: ComponentMapper<CompPrettyUi>

    override fun process(entityId: Int) {
        val space = mSpace[entityId]
        val ui = mUi[entityId]

        if (Gdx.input.justTouched()) {
            val xTouch = (GResolution.baseX + GHelper.x) - space.x
            val yTouch = (GResolution.baseY + GHelper.y) - space.y
            ui.anims.add(FontAnim(ui.pixels.sortedBy { Vector2.dst(xTouch, yTouch, it.x, it.y) }.toMutableList(), xTouch, yTouch))
        }
        ui.time += GTime.delta
        ui.currentIndex = (ui.time * 30).toInt()
        ui.pixels.filterIndexed { index, pixel ->
            index < ui.currentIndex
        }.forEach { pixel ->
            pixel.act(Gdx.graphics.deltaTime)
            pixel.draw(space.x, space.y)
        }
        ui.anims.removeIf { it.act() }
    }

}