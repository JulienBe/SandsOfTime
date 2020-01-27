package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.GSide

class CompSide : Comp() {

    var side = GSide.NONE

    override fun reset() {
        super.reset()
        side = GSide.NONE
    }
}