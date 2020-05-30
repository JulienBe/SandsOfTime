package be.particulitis.hourglass.comp.draw

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CompUndertrail : Comp() {
    lateinit var tr: TextureRegion
    var draw = { batch: GGraphics, space: CompSpace ->
        batch.drawStreched(tr, space)
    }

    override fun reset() {
        super.reset()
        draw = { batch: GGraphics, space: CompSpace ->
            batch.drawStreched(tr, space)
        }
    }

}