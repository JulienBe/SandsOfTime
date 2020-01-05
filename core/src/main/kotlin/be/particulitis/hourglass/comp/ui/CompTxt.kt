package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.font.FontPixel
import com.badlogic.gdx.math.Vector2
import ktx.collections.GdxArray
import ktx.collections.addAll
import kotlin.math.roundToInt

open class CompTxt : Comp() {

    var text: String = ""
        internal set
    val pixels = GdxArray<FontPixel>()

    fun set(txt: String) {
        if (txt != text) {
            changeText(txt)
        }
    }

    fun changeText(txt: String, w: Int = 1, speed: Float = 10f) {
        this.text = txt
        val sort = pixels.isEmpty
        pixels.addAll(txt.mapIndexed { index, c -> FontPixel.get(index, c, w, pixels) }.flatten())
        if (sort)
            pixels.sort { pixel, pixel2 ->
                (Vector2.dst2(pixel.x, pixel.y, GResolution.screenWidth / 2f, GResolution.screenHeight / 2f) -
                        Vector2.dst2(pixel2.x, pixel2.y, GResolution.screenWidth / 2f, GResolution.screenHeight / 2f)).roundToInt()
            }
        for (i in 1 until w)
            pixels.forEach {
                it.desiredX -= FontPixel.charWidth * 4
            }
    }

    override fun reset() {
        super.reset()
        changeText("")
    }

}