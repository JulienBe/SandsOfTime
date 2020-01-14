package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.World
import com.badlogic.gdx.Gdx

object SProps : Setup() {
    fun wall(world: World, tilesWidth: Int, tilesHeight: Int, offsetX: Float = 0f, offsetY: Float = 0f) {
        for (x in 0 until tilesWidth)
            for (y in 0 until tilesHeight) {
                val wall = world.create(Builder.wall)
                val space = wall.space()
                val draw = wall.draw()
                val tr = GGraphics.imgMan.walls.random()
                draw.texture = tr
                draw.normal = GGraphics.imgMan.nor(tr.toString())
                space.setPos(offsetX + x * draw.texture.regionWidth, offsetY + y * draw.texture.regionHeight)
                draw.drawFront = {
                    DrawMethods.drawFront(space, draw, it)
                }
                draw.drawNormal = {
                    DrawMethods.drawNor(space, draw, it)
                }
            }
    }

    fun dent(world: World, depth: Int, x: Float, y: Float) {
        val dent = world.create(Builder.dent)
        val space = dent.space()
        val draw = dent.draw()
        draw.normal = GGraphics.nor(ImgMan.dent)
        space.setPos(x, y)
        draw.drawFront = {}
        draw.drawNormal = {
            DrawMethods.drawNor(space, draw, it)
        }
    }
}