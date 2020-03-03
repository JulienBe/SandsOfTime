package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GImage
import be.particulitis.hourglass.gamedata.graphics.Colors

class CompDraw : Comp() {
    lateinit var currentImg: GImage
    var color = Colors.explosion
    var preDraw = { }
    var drawFront = { batch: GGraphics, space: CompSpace ->
        batch.drawFrontCenteredOnBox(this, space)
    }
    var drawNormal = { batch: GGraphics, space: CompSpace ->
        batch.drawNormalCenteredOnBox(this, space)
    }
    var layer = 0
    var angle = 0f

    override fun reset() {
        super.reset()
        preDraw = { }
        angle = 0f
        drawFront = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontCenteredOnBox(this, space)
        }
        drawNormal = { batch: GGraphics, space: CompSpace ->
            batch.drawNormalCenteredOnBox(this, space)
        }
    }

}