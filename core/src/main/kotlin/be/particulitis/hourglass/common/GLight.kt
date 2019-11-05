package be.particulitis.hourglass.common

object GLight {

    val xy = LinkedHashMap<Int, Float>()
    val rgb = LinkedHashMap<Int, Float>()
    val intensity = LinkedHashMap<Int, Float>()
    var id = 0

    fun create(x: Float, y: Float, r: Float, g: Float, b: Float, intensity: Float): Int {
        id++
        this.intensity[id] = intensity
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