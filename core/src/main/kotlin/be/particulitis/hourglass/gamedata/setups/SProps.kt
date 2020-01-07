package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import com.artemis.World

object SProps : Setup() {
    fun wall(world: World, tilesWidth: Int, tilesHeight: Int, offsetX: Float = 0f, offsetY: Float = 0f) {
        for (x in 0..tilesWidth)
            for (y in 0..tilesHeight) {
                val wall = world.create(Builder.wall)
                val space = wall.space()
                val draw = wall.draw()
                draw.texture = GGraphics.imgMan.walls.random()
                space.setPos(offsetX + x * draw.texture.regionWidth, offsetY + y * draw.texture.regionHeight)
                draw.drawingStyle = {
                    it.drawWhite(draw.texture, space.x, space.y)
                }
            }
    }
}