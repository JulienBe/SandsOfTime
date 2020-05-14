package be.particulitis.hourglass.comp.ui

import be.particulitis.hourglass.font.UiAnim
import ktx.collections.GdxArray

class CompPrettyUi : CompTxt() {

    internal var currentIndex = 0
    internal var time = .1f
    var phases = GdxArray<UiAnim>()

    override fun reset() {
        super.reset()
        phases.clear()
    }
}