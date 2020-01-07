package be.particulitis.hourglass.common.drawing

object GLight {

    val xy = LinkedHashMap<Int, Float>()
    val rgb = LinkedHashMap<Int, Float>()
    val intensity = LinkedHashMap<Int, Float>()
    var id = 0
    fun x(id: Int) = xy[id * 2 + 0] ?: 5f
    fun y(id: Int) = xy[id * 2 + 1] ?: 5f
    fun r(id: Int) = rgb[id * 3 + 0] ?: 1f
    fun g(id: Int) = rgb[id * 3 + 1] ?: 1f
    fun b(id: Int) = rgb[id * 3 + 2] ?: 1f
    fun intensity(id: Int) = intensity[id] ?: 1f

    fun create(x: Float, y: Float, palette: GPalette, intensity: Float): Int {
        return create(x, y, palette.r, palette.g, palette.b, intensity)
    }

    fun create(x: Float, y: Float, r: Float, g: Float, b: Float, intensity: Float): Int {
        id++
        GLight.intensity[id] = intensity
        xy[id * 2 + 0] = x
        xy[id * 2 + 1] = y
        rgb[id * 3 + 0] = r
        rgb[id * 3 + 1] = g
        rgb[id * 3 + 2] = b

        return id
    }

    fun numberOfLights() = intensity.size

    fun clear() {
        intensity.clear()
        xy.clear()
        rgb.clear()
        id = 0
    }

    fun delete(id: Int) {
        xy.remove(id * 2 + 0)
        xy.remove(id * 2 + 1)
        rgb.remove(id * 3 + 0)
        rgb.remove(id * 3 + 1)
        rgb.remove(id * 3 + 2)
        intensity.remove(id)
    }

    fun updatePos(id: Int, x: Float, y: Float) {
        xy[id * 2 + 0] = x
        xy[id * 2 + 1] = y
    }
}