package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette

class CompLight : Comp() {

    var id = -1

    fun setLight(palette: GPalette, x: Float, y: Float, intensity: Float, angle: Float = 0f, tilt: Float = 0f) {
        id = GLight.create(x, y, palette.r, palette.g, palette.b, intensity, angle, tilt)
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
        ifValid {
            GLight.updatePos(id, x, y)
        }
    }

    fun updatePosAngle(x: Float, y: Float, angle: Float) {
        ifValid {
            GLight.updatePosAngle(id, x, y, angle)
        }
    }

    fun updateIntesity(i: Float) {
        ifValid {
            GLight.updateIntensity(id, i)
        }
    }

    fun updateTilt(tilt: Float) {
        ifValid {
            GLight.updateTilt(id, tilt)
        }
    }

    private fun ifValid(update: () -> Unit) {
        if (id != -1)
            update.invoke()
    }

}