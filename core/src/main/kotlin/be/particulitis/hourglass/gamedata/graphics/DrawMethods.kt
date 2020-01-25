package be.particulitis.hourglass.gamedata.graphics

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompOccluder
import be.particulitis.hourglass.comp.CompSpace
import kotlin.math.roundToInt

object DrawMethods {
    fun basic(space: CompSpace, draw: CompDraw, batch: GGraphics) {
        batch.draw(space, draw)
    }

    fun drawNor(space: CompSpace, draw: CompDraw, batch: GGraphics) {
        batch.draw(draw.normal, space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat())
    }
    fun drawFrontAngle(space: CompSpace, draw: CompDraw, batch: GGraphics) {
        batch.draw(draw.texture, space, draw.normalAngle)
    }
    fun drawNorAngle(space: CompSpace, draw: CompDraw, batch: GGraphics) {
        batch.draw(draw.normal, space, draw.normalAngle)
    }
    fun drawFront(space: CompSpace, draw: CompDraw, batch: GGraphics) {
        batch.draw(draw.texture, space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat())
    }
    fun drawOcc(space: CompSpace, occ: CompOccluder, batch: GGraphics) {
        batch.draw(occ.texture, space.x.roundToInt().toFloat(), space.y.roundToInt().toFloat())
    }
}