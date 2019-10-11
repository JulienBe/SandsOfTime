package be.particulitis.hourglass.comp

import be.particulitis.hourglass.FontPixel
import ktx.collections.GdxArray
import ktx.collections.addAll

class CompTxt : Comp() {

    var text: String = ""
        private set
    val pixels = GdxArray<FontPixel>()

    fun set(txt: String) {
        if (txt != text) {
            text = txt
            pixels.clear()
            txt.forEachIndexed { index, c -> pixels.addAll(FontPixel.get(index, c)!!) }
        }
    }

    override fun reset() {
        super.reset()
        text = ""
    }

}