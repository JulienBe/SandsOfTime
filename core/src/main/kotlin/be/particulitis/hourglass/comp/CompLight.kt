package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette

class CompLight : Comp() {

    var id = -1

    fun setLight(palette: GPalette, x: Float, y: Float, intensity: Float) {
        id = GLight.create(x, y, palette.r, palette.g, palette.b, intensity)
    }

    fun clear() {
        if (id != -1)
            GLight.delete(id)
        id = -1
    }

    override fun reset() {
        super.reset()
        clear()
    }

    fun updatePos(x: Float, y: Float) {
        if (id != -1)
            GLight.updatePos(id, x, y)
    }
}