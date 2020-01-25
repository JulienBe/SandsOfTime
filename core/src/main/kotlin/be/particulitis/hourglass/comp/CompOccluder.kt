package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.drawing.GGraphics
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CompOccluder : Comp() {
    lateinit var texture: TextureRegion
    var draw = { batch: GGraphics ->
    }

    override fun reset() {
        super.reset()
        draw = { batch: GGraphics->
        }
    }
}