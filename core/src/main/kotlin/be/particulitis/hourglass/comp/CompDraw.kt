package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.graphics.Colors
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CompDraw : Comp() {
    lateinit var texture: TextureRegion
    var color = Colors.explosion
    var drawingStyle = { _: GGraphics -> }
    var layer = 0
    var cpt = 0
    var accu = 0f

    override fun reset() {
        super.reset()
        drawingStyle = {}
        cpt = 0
    }
}