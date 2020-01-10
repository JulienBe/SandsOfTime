package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.graphics.Colors
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CompDraw : Comp() {
    lateinit var texture: TextureRegion
    lateinit var normal: TextureRegion
    var color = Colors.explosion
    var drawingStyle = { _: GGraphics, tr: TextureRegion -> }
    var layer = 0
    var cpt = 0
    var accu = 0f

    override fun reset() {
        super.reset()
        drawingStyle = {batch, tr -> }
        cpt = 0
    }
}