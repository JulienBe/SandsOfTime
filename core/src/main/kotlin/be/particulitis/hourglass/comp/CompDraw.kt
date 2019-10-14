package be.particulitis.hourglass.comp

import be.particulitis.hourglass.builds.Colors

class CompDraw : Comp() {
    var color = Colors.scoreFont
    var drawingStyle = DrawStyle.NONE
    var layer = 0

    override fun reset() {
        super.reset()
        drawingStyle = DrawStyle.NONE
    }
}

enum class DrawStyle {
    DIR_TRAIL,
    NONE
}