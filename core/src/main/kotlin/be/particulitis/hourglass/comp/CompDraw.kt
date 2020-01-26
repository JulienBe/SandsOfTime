package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.graphics.Colors
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CompDraw : Comp() {
    lateinit var texture: TextureRegion
    lateinit var normal: TextureRegion
    var color = Colors.explosion
    var drawFront = { _: GGraphics -> }
    var drawNormal = { _: GGraphics -> }
    var layer = 0
    var cpt = 0
    var angle = 0f

    override fun reset() {
        super.reset()
        drawFront = { }
        drawNormal = { }
        cpt = 0
        angle = 0f
    }

}