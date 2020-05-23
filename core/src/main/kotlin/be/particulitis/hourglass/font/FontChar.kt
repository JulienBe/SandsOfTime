package be.particulitis.hourglass.font

import ktx.collections.GdxArray
import ktx.collections.toGdxArray

class FontChar(var char: Char, val pixels: GdxArray<FontPixel>) {

    constructor(char: Char, pixels: List<FontPixel>) : this(char, pixels.toGdxArray())

    fun changeChar(c: Char, index: Int, w: Int) {
        val mainColor = pixels.first().myMainColor
        val secondaryColor = pixels.first().mySecondaryColor
        val newPixels = FontPixel.get(index, c, w, pixels)
        pixels.clear()
        pixels.addAll(newPixels.toGdxArray())
        pixels.forEach {
            it.initDelay = 0f
            it.updateColor(mainColor, secondaryColor)
        }
    }
}
