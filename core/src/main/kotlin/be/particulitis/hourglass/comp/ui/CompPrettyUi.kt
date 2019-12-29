package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.font.FontAnim
import be.particulitis.hourglass.font.FontPixel
import ktx.collections.GdxArray
import ktx.collections.addAll

class CompPrettyUi : Comp() {

    internal var pixels = GdxArray<FontPixel>()
    internal var currentIndex = 0
    internal var time = .1f
    internal var anims = arrayListOf<FontAnim>()

    fun setText(txt: String) {
        pixels.clear()
        pixels.addAll(txt.mapIndexed { index, c -> FontPixel.get(index, c) }.flatten())
        pixels.forEach {
            it.x = it.desiredX + GRand.gauss(70f)
            it.y = it.desiredY + GRand.gauss(70f)
        }
    }

}