package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.font.FontAnim
import be.particulitis.hourglass.font.FontPixel
import com.badlogic.gdx.math.Vector2
import ktx.collections.GdxArray
import ktx.collections.addAll
import kotlin.math.roundToInt

class CompPrettyUi : Comp() {

    internal var pixels = GdxArray<FontPixel>()
    internal var currentIndex = 0
    internal var time = .1f
    internal var anims = arrayListOf<FontAnim>()
    internal var txt = ""
    internal var w = 1

    fun setText(txt: String, w: Int = 1) {
        this.txt = txt
        this.w = w
        val sort = pixels.isEmpty
        pixels.addAll(txt.mapIndexed { index, c -> FontPixel.get(index, c, w, pixels) }.flatten())
        if (sort)
            pixels.sort { pixel, pixel2 ->
                (Vector2.dst2(pixel.x, pixel.y, GResolution.screenWidth / 2f, GResolution.screenHeight / 2f) -
                        Vector2.dst2(pixel2.x, pixel2.y, GResolution.screenWidth / 2f, GResolution.screenHeight / 2f)).roundToInt()
            }
    }

}