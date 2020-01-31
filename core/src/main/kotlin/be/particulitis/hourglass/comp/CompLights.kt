package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette

class CompLights : Comp() {

    var lastId = -1
        private set

    fun setLight(palette: GPalette, x: Float, y: Float, intensity: Float, angle: Float = 0f, tilt: Float = 0f): Int {
        lastId = GLight.create(x, y, palette.r, palette.g, palette.b, intensity, angle, tilt)
        return lastId
    }

    fun clear() {
        if (lastId != -1)
            GLight.delete(lastId)
        lastId = -1
    }

    override fun reset() {
        super.reset()
        clear()
    }

    fun updatePos(x: Float, y: Float, id: Int = lastId) {
        ifValid(id) {
            GLight.updatePos(id, x, y)
        }
    }

    fun updatePosAngle(x: Float, y: Float, angle: Float, id: Int = lastId) {
        ifValid(id) {
            GLight.updatePosAngle(id, x, y, angle)
        }
    }

    fun updateIntesity(i: Float, id: Int = lastId) {
        ifValid(id) {
            GLight.updateIntensity(id, i)
        }
    }

    fun updateTilt(tilt: Float, id: Int = lastId) {
        ifValid(id) {
            GLight.updateTilt(id, tilt)
        }
    }

    private fun ifValid(id: Int, update: () -> Unit) {
        if (id != -1)
            update.invoke()
    }

}