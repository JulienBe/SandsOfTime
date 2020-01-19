package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.math.MathUtils

object GLight {

    val xyat = LinkedHashMap<Int, Float>()
    val rgb = LinkedHashMap<Int, Float>()
    val intensity = LinkedHashMap<Int, Float>()
    var id = 0

    fun x(id: Int) = xyat[id * 4 + 0] ?: 5f
    fun y(id: Int) = xyat[id * 4 + 1] ?: 5f
    fun a(id: Int) = xyat[id * 4 + 2] ?: 0f
    fun i(id: Int) = xyat[id * 4 + 3] ?: 0f

    fun r(id: Int) = rgb[id * 3 + 0] ?: 1f
    fun g(id: Int) = rgb[id * 3 + 1] ?: 1f
    fun b(id: Int) = rgb[id * 3 + 2] ?: 1f
    fun intensity(id: Int) = intensity[id] ?: 1f

    fun create(x: Float, y: Float, palette: GPalette, intensity: Float, angle: Float = 1f, tilt: Float = 1f): Int {
        return create(x, y, palette.r, palette.g, palette.b, intensity, angle, tilt)
    }

    fun create(x: Float, y: Float, r: Float, g: Float, b: Float, intensity: Float, angle: Float = 0f, tilt: Float = 0f): Int {
        GLight.intensity[id] = intensity

        updatePosAngle(id, x, y, angle)
        xyat[id * 4 + 3] = tilt

        rgb[id * 3 + 0] = r
        rgb[id * 3 + 1] = g
        rgb[id * 3 + 2] = b
        id++

        return id - 1
    }

    fun numberOfLights() = intensity.size

    fun clear() {
        intensity.clear()
        xyat.clear()
        rgb.clear()
        id = 0
    }

    fun delete(id: Int) {
        xyat.remove(id * 4 + 0)
        xyat.remove(id * 4 + 1)
        xyat.remove(id * 4 + 2)
        xyat.remove(id * 4 + 3)
        rgb.remove(id * 3 + 0)
        rgb.remove(id * 3 + 1)
        rgb.remove(id * 3 + 2)
        intensity.remove(id)
    }

    fun updatePos(id: Int, x: Float, y: Float) {
        xyat[id * 4 + 0] = x
        xyat[id * 4 + 1] = y
    }
    fun updatePosAngle(id: Int, x: Float, y: Float, angle: Float) {
        xyat[id * 4 + 0] = x
        xyat[id * 4 + 1] = y
        xyat[id * 4 + 2] = angle / MathUtils.radiansToDegrees
    }

    fun updateIntensity(id: Int, i: Float) {
        intensity[id] = i
    }

    fun updateTilt(id: Int, tilt: Float) {
        xyat[id * 4 + 3] = tilt
    }
}