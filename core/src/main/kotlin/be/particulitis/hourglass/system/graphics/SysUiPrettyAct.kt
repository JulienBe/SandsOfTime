package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.font.FontAnim
import be.particulitis.hourglass.font.FontPixel
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import kotlin.math.*

@Wire(failOnNull = false)
class SysUiPrettyAct : IteratingSystem(Aspect.all(CompSpace::class.java, CompPrettyUi::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mUi: ComponentMapper<CompPrettyUi>

    override fun process(entityId: Int) {
        val space = mSpace[entityId]
        val ui = mUi[entityId]

        if (Gdx.input.justTouched()) {
            val xTouch = (GResolution.baseX + GHelper.x) - space.x
            val yTouch = (GResolution.baseY + GHelper.y) - space.y
            ui.anims.add(FontAnim(ui.pixels.sortedBy { Vector2.dst(xTouch, yTouch, it.x, it.y) }.toMutableList()))
        }
        ui.time += GTime.delta
        ui.currentIndex = ((ui.time * 5f) * ui.pixels.size / 12f).roundToInt()
        onEachPixel(ui) { pixel: FontPixel ->
            pixel.act(GTime.delta)
        }
        onEachPixel(ui) { pixel: FontPixel ->
            pixel.boost = false
            //GLight.updatePos(pixel.lightId, pixel.x + space.x, pixel.y + space.y)
        }

        ui.anims.removeIf { it.act() }
        if (ui.time > ui.currentPhase.endTime) {
            ui.currentIndex = ui.pixels.size
            ui.time = 1f
            ui.changePhase(ui.phase + 1)
        }
        if (ui.time > 15f)
            chopPixels(ui)
        if (GRand.nextInt(300) == 0 && ui.currentIndex > ui.pixels.size)
            swapPixel(ui)
    }

    private fun maybeAddLight(ui: CompPrettyUi) {
        if (ui.pixels.filter { it.lightId != -1 }.count() < (ui.pixels.size / 10))
            ui.pixels.random().initLight()
    }

    private fun onEachPixel(ui: CompPrettyUi, inside: (pixel: FontPixel) -> Unit) {
        ui.pixels.filterIndexed { index, _ ->
            index < ui.currentIndex
        }.forEach {
            inside.invoke(it)
        }
    }

    private fun swapPixel(ui: CompPrettyUi) {
        val one = ui.pixels.random()
        val two = ui.pixels.random()
        val oneFutureX = two.desiredX
        val oneFutureY = two.desiredY
        two.move(one.desiredX, one.desiredY)
        one.move(oneFutureX, oneFutureY)
    }

    private fun chopPixels(ui: CompPrettyUi) {
        val candidate = ui.pixels.random()
        if (candidate.couldBeRemoved)
            ui.pixels.removeValue(candidate, true)
    }

}