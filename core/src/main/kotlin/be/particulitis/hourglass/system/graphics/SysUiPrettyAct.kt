package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.font.FontPixel
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import kotlin.math.roundToInt

@Wire(failOnNull = false)
class SysUiPrettyAct : IteratingSystem(Aspect.all(CompSpace::class.java, CompPrettyUi::class.java)) {

    private lateinit var mUi: ComponentMapper<CompPrettyUi>
    private lateinit var mSpace: ComponentMapper<CompSpace>

    override fun process(entityId: Int) {
        val ui = mUi[entityId]
        val space = mSpace[entityId]

        ui.time += GTime.delta
        ui.currentIndex = ((ui.time * 5f) * ui.allFontPixels.size / 12f).roundToInt()
        ui.phases.forEach {
            it.trigger(ui)
            if (it.active && !it.finished)
                it.act(ui, space)
        }
        onEachPixel(ui) { pixel: FontPixel ->
            pixel.act(GTime.delta)
        }
        onEachPixel(ui) { pixel: FontPixel ->
            pixel.boost = false
        }

    }

    private fun onEachPixel(ui: CompPrettyUi, inside: (pixel: FontPixel) -> Unit) {
        ui.allFontPixels.filterIndexed { index, _ ->
            index < ui.currentIndex
        }.forEach {
            inside.invoke(it)
        }
    }

}