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
    internal var phase = 0
    internal var phases = arrayOf(
            Phase(1, 10f),
            Phase(2, 20f),
            Phase(3, 99999999999999999f)
    )
    internal val currentPhase: Phase
        get() { return phases[phase] }

    fun setText(txt: String, w: Int) {
        this.txt = txt
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

    fun changePhase(phase: Int) {
        this.phase = phase
        setText(txt, phases[phase].w)
    }

    data class Phase(val w: Int, val endTime: Float)
}