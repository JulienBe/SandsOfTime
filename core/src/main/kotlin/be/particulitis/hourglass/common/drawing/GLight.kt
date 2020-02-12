package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxSet

class GLight(x: Float, y: Float, intensity: Float, angle: Float = 0f, tilt: Float = 0f) {

    val id: Int

    init {
        id = if (idPool.isEmpty) {
            globalId++
            (globalId - 1)
        } else {
            idPool.first()
        }
        idPool.remove(id)

        GLight.intensity[id] = intensity

        updatePosAngle(x, y, angle)
        xyat[id * 4 + 3] = tilt
    }

    fun clear() {
        xyat.remove(id * 4 + 0)
        xyat.remove(id * 4 + 1)
        xyat.remove(id * 4 + 2)
        xyat.remove(id * 4 + 3)
        intensity.remove(id)
        idPool.add(id)
    }

    fun updatePos(x: Float, y: Float) {
        xyat[id * 4 + 0] = x
        xyat[id * 4 + 1] = y
    }

    fun updatePosAngle(x: Float, y: Float, angle: Float) {
        xyat[id * 4 + 0] = x
        xyat[id * 4 + 1] = y
        xyat[id * 4 + 2] = angle / MathUtils.radiansToDegrees
    }

    fun updateIntesity(i: Float) {
        intensity[id] = i
    }

    fun updateTilt(tilt: Float) {
        xyat[id * 4 + 3] = tilt
    }

    companion object {
        val xyat = LinkedHashMap<Int, Float>()
        val intensity = LinkedHashMap<Int, Float>()
        var globalId = 0

        private var idPool = GdxSet<Int>()

        fun x(id: Int) = xyat[id * 4 + 0] ?: 5f
        fun y(id: Int) = xyat[id * 4 + 1] ?: 5f
        fun a(id: Int) = xyat[id * 4 + 2] ?: 0f
        fun i(id: Int) = xyat[id * 4 + 3] ?: 0f

        fun intensity(id: Int) = intensity[id] ?: 1f

        fun numberOfLights() = intensity.size - 1

        fun clear() {
            intensity.clear()
            xyat.clear()
            globalId = 0
            idPool.clear()
        }

    }
}