package be.particulitis.hourglass.common

object GLight {

    var xy = floatArrayOf()
    var rgb = floatArrayOf()

    fun create(x: Float, y: Float, r: Float, g: Float, b: Float): Int {
        xy = xy.plus(floatArrayOf(x, y))
        rgb = rgb.plus(floatArrayOf(r, g, b))
        return numberOfLights() - 1
    }

    fun numberOfLights() = (xy.size / 2)

    fun clear() {
        xy = floatArrayOf()
        rgb = floatArrayOf()
    }
}