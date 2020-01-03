package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.font.FontPixel
import ktx.collections.GdxArray
import ktx.collections.addAll

open class CompTxt : Comp() {

    var text: String = ""
        private set
    val pixels = GdxArray<FontPixel>()

    fun set(txt: String) {
        if (txt != text) {
            changeText(txt)
        }
    }

    private fun changeText(txt: String) {
        text = txt
        pixels.clear()
        txt.forEachIndexed { index, c -> pixels.addAll(FontPixel.get(index, c)) }
    }

    override fun reset() {
        super.reset()
        changeText("")
    }

}