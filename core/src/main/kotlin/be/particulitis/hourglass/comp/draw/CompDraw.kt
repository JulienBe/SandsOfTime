package be.particulitis.hourglass.comp.draw

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GImage
import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.comp.CompSpace

class CompDraw : Comp() {
    lateinit var currentImg: GImage
    var preDraw = { }
    var drawFront = { batch: GGraphics, space: CompSpace ->
        batch.drawFrontCenteredOnBoxSpaceStreched(this, space)
    }
    var drawNormal = { batch: GGraphics, space: CompSpace ->
        batch.drawNormalCenteredOnBoxSpaceStreched(this, space)
    }
    var drawOcc = { batch: GGraphics, space: CompSpace ->
        batch.drawOccCenteredOnBoxSpaceStreched(this, space)
    }
    var layer = 0
    var angle = 0f

    override fun reset() {
        super.reset()
        preDraw = { }
        angle = 0f
        drawFront = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontCenteredOnBoxSpaceStreched(this, space)
        }
        drawNormal = { batch: GGraphics, space: CompSpace ->
            batch.drawNormalCenteredOnBoxSpaceStreched(this, space)
        }
        drawOcc = { batch: GGraphics, space: CompSpace ->
            batch.drawOccCenteredOnBoxSpaceStreched(this, space)
        }
    }

}