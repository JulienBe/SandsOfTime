package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.font.FontPixel
import ktx.collections.GdxArray

class CompButton : CompTxt() {
    var onClick = {}
    val outline = GdxArray<FontPixel>()
    var selectedHighlightIndex = 0
    var selected = false
    var deactivateOnClick = false

    override fun reset() {
        super.reset()
        onClick = {}
        outline.clear()
        selectedHighlightIndex = 0
        selected = false
        deactivateOnClick = false
    }

    fun activateReset() {
        reset()
    }

    override fun x(space: CompSpace): Float {
        return space.x - 2f
    }
    override fun y(space: CompSpace): Float {
        return space.y - 4f
    }
    override fun displayW(): Float {
        return w.toFloat() + 2f
    }
    override fun displayH(): Float {
        return h.toFloat() + 6f
    }
}