package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.graphics.Color

enum class GPalette(val r: Float, val g: Float, val b: Float) {

    BLACK(0f, 0f, 0f),
    DARK_BLUE(0.1137f,  0.169f, 0.325f),
    DARK_PINK(0.494f, 0.145f, 0.325f),
    DARK_GREEN(0f, 0.529f, 0.318f),
    BROWN(0.671f, 0.322f, 0.212f),
    DARK_GRAY(0.373f, 0.341f, 0.31f),
    LIGHT_GRAY(0.761f, 0.765f, 0.78f),
    WHITEISH(1f, 0.945f, 0.91f),
    REDISH(1f, 0f, 0.302f),
    ORANGE(1f, 0.639f, 0f),
    YELLOW(1f, 0.925f, 0.153f),
    GREEN(0f, 0.894f, 0.212f),
    BLUE(0.161f, 0.678f, 1f),
    LIGHT_PURPLE(0.514f, 0.463f, 0.612f),
    PINK(1f, 0.467f, 0.659f),
    PINK_SKIN(1f, 0.8f, 0.667f);

    val color = Color(r, g, b, 1f)
    val f = color.toFloatBits()

    fun next(): GPalette {
        return values()[(ordinal + 1) % values().size]
    }

    companion object {
        fun rand(): GPalette {
            return values().random()
        }
    }

}