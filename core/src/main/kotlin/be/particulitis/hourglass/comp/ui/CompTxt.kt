package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.font.FontChar
import be.particulitis.hourglass.font.FontPixel
import com.badlogic.gdx.math.Vector2
import ktx.collections.GdxArray
import ktx.collections.addAll
import kotlin.math.roundToInt

open class CompTxt : Comp() {

    var text: String = ""
        internal set
    val chars = GdxArray<FontChar>()
    var allFontPixels = GdxArray<FontPixel>()
    var w = 1
    var h = 1
    var fontW = 1

    fun set(txt: String, w: Int) {
        if (txt != text) {
            changeText(txt, w)
        }
    }

    fun changeText(txt: String, w: Int = 1) {
        this.text = txt
        fontW = w
        while (chars.size > txt.length)
            chars.drop(chars.size - 1)
        txt.mapIndexed {
            index, c ->
            if (chars.size < index)
                chars.add(FontChar(c, FontPixel.get(index, c, w, chars[index].pixels)))
            else
                chars.add(FontChar(c, FontPixel.get(index, c, w)))
        }
        updatePixelArray()
        allFontPixels.sort { pixel, pixel2 ->
            (Vector2.dst2(pixel.x.get(), pixel.y.get(), GResolution.areaW, GResolution.areaHH) -
                    Vector2.dst2(pixel2.x.get(), pixel2.y.get(), GResolution.areaW, GResolution.areaHH)).roundToInt()
        }
        this.w = (txt.length * w * 3) + txt.length
        this.h = (w * 5) - 1
    }

    private fun updatePixelArray() {
        allFontPixels.clear()
        allFontPixels.addAll(chars.flatMap { c -> c.pixels })
    }

    fun updateText(s: String, w: Int) {
        assert(s.length == text.length)
        for (i in s.indices)
            if (s[i] != text[i])
                chars[i].changeChar(s[i], i, w)
        updatePixelArray()
        text = s
    }

    open fun x(space: CompSpace): Float {
        return space.x
    }
    open fun y(space: CompSpace): Float {
        return space.y
    }
    open fun displayW(): Float {
        return w.toFloat()
    }
    open fun displayH(): Float {
        return h.toFloat()
    }

    override fun reset() {
        text = ""
        chars.clear()
        allFontPixels.clear()
        super.reset()
    }

}