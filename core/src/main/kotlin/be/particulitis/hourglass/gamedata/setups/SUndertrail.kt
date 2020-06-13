package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.create
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.space
import be.particulitis.hourglass.undertrail
import com.artemis.World

object SUndertrail {

    fun dot(x: Float, y: Float, palette: GPalette, world: World) {
        val d = world.create(Builder.underDot)
        val space = d.space()
        val draw = d.undertrail()
        space.setPos(x, y)
        space.setDim(1f, 1f)
        draw.tr = palette.tr
        draw.draw = { batch, space ->
            batch.drawStreched(draw.tr, space)
            world.deleteEntity(d)
        }
    }

}