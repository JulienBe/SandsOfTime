package be.particulitis.hourglass.font

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import com.badlogic.gdx.math.Vector2
import ktx.collections.toGdxArray
import kotlin.math.min

class FlipColorAnim: UiAnim() {

    private var flipIndex = 0
    private var mainColor = GPalette.rand().tr
    private var secondaryColor = GPalette.rand().tr
    private var nextTrigger = 3f

    override fun trigger(ui: CompPrettyUi) {
        active = ui.time > nextTrigger
    }
    override fun act(ui: CompPrettyUi, space: CompSpace) {
        for (i in 0..ui.pixels.size / 200) {
            val p = ui.pixels[flipIndex % ui.pixels.size]
            if (p.primary)
                p.tr = mainColor
            else
                p.tr = secondaryColor
            if (GRand.bool())
                p.y.add(p.y.get() + GRand.gauss(.25f))
            else
                p.x.add(p.x.get() + GRand.gauss(.25f))
            flipIndex++
        }
        if (flipIndex > ui.pixels.size) {
            changeColor()
            flipIndex = 0
            nextTrigger = ui.time + 3f
            val randomX = GRand.randX()
            val randomY = GRand.randY()
            ui.pixels.sort { pixel, pixel2 ->
                (Vector2.dst2(pixel.x.get() + space.x, pixel.y.get() + space.y, randomX, randomY) - Vector2.dst2(pixel2.x.get() + space.x, pixel2.y.get() + space.y, randomX, randomY)).toInt()
            }
            val splitSize = ui.pixels.size / 20
            for (i in 0 until 20)
                sort(ui, 0, splitSize, space, GRand.randX(), GRand.randY())
            // you don't want the leftover to be in the initial order
            sort(ui, 0, (ui.pixels.size % 20), space, GRand.randX(), GRand.randY())
        }
    }

    private fun changeColor() {
        mainColor = GPalette.rand().tr
        secondaryColor = GPalette.rand().tr
    }

    private fun partialSort(ui: CompPrettyUi, space: CompSpace) {
        val randomX = GRand.randX()
        val randomY = GRand.randY()
        val from = GRand.int(0, ui.pixels.size)
        val to = min(ui.pixels.size - 1, from + (ui.pixels.size / 5))
        sort(ui, from, to, space, randomX, randomY)
    }

    private fun sort(ui: CompPrettyUi, from: Int, to: Int, space: CompSpace, randomX: Float, randomY: Float) {
        val sublist = ui.pixels.toList().subList(from, to).sortedBy { pixel ->
            Vector2.dst2(pixel.x.get() + space.x, pixel.y.get() + space.y, randomX, randomY)
        }
        ui.pixels.removeRange(from, to - 1)
        ui.pixels.addAll(sublist.toGdxArray())
    }

}
