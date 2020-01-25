package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.World

object SProps : Setup() {
    fun ground(world: World, tilesWidth: Int, tilesHeight: Int, offsetX: Float = 0f, offsetY: Float = 0f) {
        createTiled(tilesWidth, tilesHeight, world, offsetX, offsetY) { "floor1" }
    }

    fun barrel(world: World, x: Float, y: Float) {
        basicProp(world, x, y, "barrel")
    }

    fun chest(world: World, x: Float, y: Float) {
        basicProp(world, x, y, "chest")
    }

    private fun basicProp(world: World, x: Float, y: Float, name: String) {
        val barrel = world.create(Builder.occluderProp)
        val space = barrel.space()
        val draw = barrel.draw()
        val occluder = barrel.occluder()

        space.setPos(x, y)
        GGraphics.setupTexturesOccluder(name, draw, occluder)
        draw.drawFront = {
            DrawMethods.drawFront(space, draw, it)
        }
        draw.drawNormal = {
            DrawMethods.drawNor(space, draw, it)
        }
        occluder.draw = {
            DrawMethods.drawOcc(space, occluder, it)
        }
        draw.layer = 2
    }

    private fun createTiled(tilesWidth: Int, tilesHeight: Int, world: World, offsetX: Float, offsetY: Float, tr: () -> String) {
        for (x in 0 until tilesWidth)
            for (y in 0 until tilesHeight) {
                val tile = world.create(Builder.tiled)
                val space = tile.space()
                val draw = tile.draw()
                val trName = tr.invoke()
                draw.texture = GGraphics.imgMan.tr(trName)
                draw.normal = GGraphics.imgMan.nor(trName)
                space.setPos(offsetX + x * draw.texture.regionWidth, offsetY + y * draw.texture.regionHeight)
                draw.drawFront = {
                    DrawMethods.drawFront(space, draw, it)
                }
                draw.drawNormal = {
                    draw.normalAngle = 0.0f
                    DrawMethods.drawNor(space, draw, it)
                }
            }
    }

    fun wall(world: World, tilesWidth: Int, tilesHeight: Int, offsetX: Float = 0f, offsetY: Float = 0f) {
        createTiled(tilesWidth, tilesHeight, world, offsetX, offsetY) {
            GGraphics.imgMan.walls.random().toString()
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