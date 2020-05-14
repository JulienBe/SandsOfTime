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
    var w = 1
    var h = 1

    fun set(txt: String, w: Int) {
        if (txt != text) {
            changeText(txt, w)
        }
    }

    fun changeText(txt: String, w: Int = 1) {
        this.text = txt
        pixels.addAll(txt.mapIndexed { index, c -> FontPixel.get(index, c, w, pixels) }.flatten())
        pixels.sort { pixel, pixel2 ->
            (Vector2.dst2(pixel.x.get(), pixel.y.get(), GResolution.areaW, GResolution.areaHH) -
                    Vector2.dst2(pixel2.x.get(), pixel2.y.get(), GResolution.areaW, GResolution.areaHH)).roundToInt()
        }
        this.w = (txt.length * w * 3) + txt.length
        this.h = (w * 5) - 1
    }

    override fun reset() {
        super.reset()
        changeText("")
    }

}