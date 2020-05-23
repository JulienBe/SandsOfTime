package be.particulitis.hourglass.font

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import com.badlogic.gdx.math.Vector2
import ktx.collections.toGdxArray
import kotlin.math.min
import kotlin.math.roundToInt

class FlipColorAnim: UiAnim() {

    private var flipIndex = 0
    private var mainColor = GPalette.rand().tr
    private var secondaryColor = GPalette.rand().tr
    private var nextTrigger = 3f

    override fun trigger(ui: CompPrettyUi) {
        active = ui.time > nextTrigger
    }
    override fun act(ui: CompPrettyUi, space: CompSpace) {
        for (i in 0..ui.allFontPixels.size / 200) {
            val p = ui.allFontPixels[flipIndex % ui.allFontPixels.size]
            p.updateColor(mainColor, secondaryColor)
            flipIndex++
        }
        if (flipIndex > ui.allFontPixels.size) {
            changeColor()
            flipIndex = 0
            nextTrigger = ui.time + 3f
            when (GRand.int(0, 3)) {
                0 -> leftRight(ui, space)
                1 -> rightLeft(ui, space)
                else -> paintColorSwap(ui, space)
            }
        }
    }

    private fun leftRight(ui: CompPrettyUi, space: CompSpace) {
        ui.allFontPixels.sort { p1, p2 ->
            (p1.x.get() - p2.x.get()).roundToInt()
        }
    }
    private fun rightLeft(ui: CompPrettyUi, space: CompSpace) {
        ui.allFontPixels.sort { p1, p2 ->
            (p2.x.get() - p1.x.get()).roundToInt()
        }
    }

    private fun paintColorSwap(ui: CompPrettyUi, space: CompSpace) {
        val randomX = GRand.randX()
        val randomY = GRand.randY()
        ui.allFontPixels.sort { pixel, pixel2 ->
            (Vector2.dst2(pixel.x.get() + space.x, pixel.y.get() + space.y, randomX, randomY) - Vector2.dst2(pixel2.x.get() + space.x, pixel2.y.get() + space.y, randomX, randomY)).toInt()
        }
        val splitSize = ui.allFontPixels.size / 20
        for (i in 0 until 20)
            sort(ui, 0, splitSize, space, GRand.randX(), GRand.randY())
        // you don't want the leftover to be in the initial order
        sort(ui, 0, (ui.allFontPixels.size % 20), space, GRand.randX(), GRand.randY())
    }

    private fun changeColor() {
        mainColor = GPalette.rand().tr
        secondaryColor = GPalette.rand().tr
    }

    private fun partialSort(ui: CompPrettyUi, space: CompSpace) {
        val randomX = GRand.randX()
        val randomY = GRand.randY()
        val from = GRand.int(0, ui.allFontPixels.size)
        val to = min(ui.allFontPixels.size - 1, from + (ui.allFontPixels.size / 5))
        sort(ui, from, to, space, randomX, randomY)
    }

    private fun sort(ui: CompPrettyUi, from: Int, to: Int, space: CompSpace, randomX: Float, randomY: Float) {
        if (to == 0)
            return
        val sublist = ui.allFontPixels.toList().subList(from, to).sortedBy { pixel ->
            Vector2.dst2(pixel.x.get() + space.x, pixel.y.get() + space.y, randomX, randomY)
        }
        ui.allFontPixels.removeRange(from, to - 1)
        ui.allFontPixels.addAll(sublist.toGdxArray())
    }

}
