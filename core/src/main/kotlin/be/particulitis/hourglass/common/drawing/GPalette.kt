package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.graphics.Color

enum class GPalette(red: Int, green: Int, blue: Int) {

    BLACK(        0,   0, 0),  // 0
    DARK_GREY(   95,  87, 79), // 182
    LIGHT_GREY( 194, 195, 199),// 389
    WHITE(      255, 241, 232),// 496
    PINK_SKIN(  255, 204, 170),// 459
    PINK(       255, 119, 168),// 374
    RED(        255,   0, 77), // 255
    DARK_PURPLE(126,  37, 83), // 163
    BROWN(      171,  82, 54), // 253
    ORANGE(     255, 163, 0),  // 418
    YELLOW(     255, 236, 39), // 491
    GREEN(        0, 228, 54), // 228
    DARK_GREEN(   0, 135, 81), // 135
    BLUE(        41, 173, 255),// 214
    LAVENDER(   131, 118, 156),// 249
    DARK_BLUE(   29,  43, 83); // 72

    val r = (1f / 255f) * red
    val g = (1f / 255f) * green
    val b = (1f / 255f) * blue
    val color = Color(r, g, b, 1f)
    val f = color.toFloatBits()
    val img = GGraphics.img("squares/square_${name.toLowerCase()}")
    val tr = img.front

    fun next(): GPalette {
        return values()[(ordinal + 1) % values().size]
    }

    companion object {
        fun rand(): GPalette {
            return values().random()
        }
    }

}