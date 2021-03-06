package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxSet

class GLight(x: Float, y: Float, intensity: Float, angle: Float = 0f, tilt: Float = 0f, r: Float = 1f, g: Float = 1f, b: Float = 1f) {

    constructor(x: Float, y: Float, intensity: Float, palette: GPalette): this(x, y, intensity, r = palette.r, g = palette.g, b = palette.b)

    val id: Int
    var cleared = false

    init {
        id = if (idPool.isEmpty) {
            globalId++
            (globalId - 1)
        } else {
            idPool.first()
        }
        idPool.remove(id)

        intensityrgb[id * 4 + 0] = intensity
        intensityrgb[id * 4 + 1] = r
        intensityrgb[id * 4 + 2] = g
        intensityrgb[id * 4 + 3] = b

        updatePosAngle(id, x, y, angle)
        xyat[id * 4 + 3] = tilt
        cleared = false
    }


    fun clear() {
        if (!cleared)
            clear(id)
        cleared = true
    }

    fun updatePos(x: Float, y: Float) {
        updatePos(id, x, y)
    }

    fun updatePosAngle(x: Float, y: Float, angle: Float) {
        updatePosAngle(id, x, y, angle)
    }

    fun updateIntesity(i: Float) {
        updateIntensity(id, i)
    }

    fun updateIntesityRGB(i: Float, r: Float, g: Float, b: Float) {
        updateIntensityRGB(id, i, r, g, b)
    }

    fun updateTilt(tilt: Float) {
        updateTilt(id, tilt)
    }

    fun updateIntesityRGB(intensity: Float, color: GPalette) {
        updateIntesityRGB(intensity, color.r, color.g, color.b)
    }

    companion object {
        val xyat = LinkedHashMap<Int, Float>()
        val intensityrgb = LinkedHashMap<Int, Float>()
        var globalId = 0
        var ambient = 0.3f

        private var idPool = GdxSet<Int>()

        fun x(id: Int) = xyat[id * 4 + 0] ?: 5f
        fun y(id: Int) = xyat[id * 4 + 1] ?: 5f
        fun a(id: Int) = xyat[id * 4 + 2] ?: 0f
        fun i(id: Int) = xyat[id * 4 + 3] ?: 0f

        fun intensity(id: Int) = intensityrgb[id * 4 + 0] ?: 1f

        fun numberOfLights(): Int = (intensityrgb.size / 4) - 1

        fun clear() {
            intensityrgb.clear()
            xyat.clear()
            globalId = 0
            idPool.clear()
        }

        fun clear(id: Int) {
            if (id != -1) {
                xyat.remove(id * 4 + 0)
                xyat.remove(id * 4 + 1)
                xyat.remove(id * 4 + 2)
                xyat.remove(id * 4 + 3)
                intensityrgb.remove(id * 4 + 0)
                intensityrgb.remove(id * 4 + 1)
                intensityrgb.remove(id * 4 + 2)
                intensityrgb.remove(id * 4 + 3)
                idPool.add(id)
            }
        }

        fun updatePos(id: Int, x: Float, y: Float) {
            if (id != -1) {
                xyat[id * 4 + 0] = x
                xyat[id * 4 + 1] = y
            }
        }

        fun updatePosAngle(id: Int, x: Float, y: Float, angle: Float) {
            if (id != -1) {
                xyat[id * 4 + 0] = x
                xyat[id * 4 + 1] = y
                xyat[id * 4 + 2] = angle / MathUtils.radiansToDegrees
            }
        }

        fun updateIntensity(id: Int, i: Float) {
            if (id != -1)
                intensityrgb[id * 4 + 0] = i
        }

        fun updateIntensityRGB(id: Int, i: Float, r: Float, g: Float, b: Float) {
            if (id != -1) {
                intensityrgb[id * 4 + 0] = i
                intensityrgb[id * 4 + 1] = r
                intensityrgb[id * 4 + 2] = g
                intensityrgb[id * 4 + 3] = b
            }
        }

        fun updateTilt(id: Int, tilt: Float) {
            if (id != -1)
                xyat[id * 4 + 3] = tilt
        }

    }
}
