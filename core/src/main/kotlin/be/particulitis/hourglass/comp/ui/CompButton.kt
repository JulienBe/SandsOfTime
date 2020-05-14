package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.font.FontPixel
import ktx.collections.GdxArray

class CompButton : CompTxt() {
    var onClick = {}
    val outline = GdxArray<FontPixel>()
    var selectedHighlightIndex = 0
    var selected = false
}