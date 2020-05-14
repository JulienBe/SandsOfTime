package be.particulitis.hourglass.font

import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompPrettyUi

open class UiAnim {
    var active = false
    var finished = false

    open fun trigger(ui: CompPrettyUi) {
    }
    open fun act(ui: CompPrettyUi, space: CompSpace) {
    }
}