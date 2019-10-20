package be.particulitis.hourglass.comp

import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.common.GBatch

class CompDraw : Comp() {
    var color = Colors.scoreFont
    var drawingStyle = {batch: GBatch -> }
    var layer = 0
    var cpt = 0
    var accu = 0f

    override fun reset() {
        super.reset()
        drawingStyle = {}
        cpt = 0
    }
}