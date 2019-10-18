package be.particulitis.hourglass.comp

import be.particulitis.hourglass.builds.Colors
import be.particulitis.hourglass.common.GBatch

class CompDraw : Comp() {
    var color = Colors.scoreFont
    var drawingStyle = {batch: GBatch -> }
    var layer = 0

    override fun reset() {
        super.reset()
        drawingStyle = {}
    }
}