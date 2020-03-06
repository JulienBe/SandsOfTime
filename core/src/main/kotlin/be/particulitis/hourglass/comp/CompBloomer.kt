package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GGraphics
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CompBloomer : Comp() {
    lateinit var tr: TextureRegion
    var preDraw = { }
    var draw = { batch: GGraphics, space: CompSpace ->
        batch.drawFront(this, space)
    }
    var angle = 0f

    override fun reset() {
        super.reset()
        preDraw = { }
        angle = 0f
        draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFront(this, space)
        }
    }

}