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
                val tr = GGraphics.imgMan.walls.random()
                draw.texture = tr
                draw.normal = GGraphics.imgMan.nor(tr.toString())
                space.setPos(offsetX + x * draw.texture.regionWidth, offsetY + y * draw.texture.regionHeight)
                draw.drawingStyle = {
                    batch, tr -> batch.drawWhite(tr, space.x, space.y)
                }
            }
    }
}