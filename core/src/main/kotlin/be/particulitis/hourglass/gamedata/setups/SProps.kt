package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.common.GSide
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.World
import ktx.collections.GdxArray

object SProps : Setup() {
    fun ground(world: World, tilesWidth: Int, tilesHeight: Int, offsetX: Float = 0f, offsetY: Float = 0f) {
        createTiled(tilesWidth, tilesHeight, world, offsetX, offsetY, Builder.tiled) { "floor1" }
    }

    fun wall(world: World, tilesWidth: Int, tilesHeight: Int, exposedSide: GSide, offsetX: Float = 0f, offsetY: Float = 0f) {
        val walls = createTiled(tilesWidth, tilesHeight, world, offsetX, offsetY, Builder.wall) {
            //GGraphics.imgMan.walls.random().toString()
            "wall1"
        }
        walls.forEach {
            val collide = it.collide()
            val space = it.space()
            val draw = it.draw()
            collide.setIds(Ids.propsWall)
            collide.addCollidingWith(Ids.player, Ids.playerBullet, Ids.enemy, Ids.enemyBullet)
            it.side().side = exposedSide
            collide.collision = { col, oSpace, side ->
                col.fromOtherCollider(collide, space, exposedSide)
            }
            draw.drawFront = {
                batch -> batch.draw(draw.texture, space, exposedSide.angle + 90f)
            }
            draw.drawNormal = {
                batch -> batch.draw(draw.normal, space, exposedSide.angle + 90f)
            }
        }
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

    private fun createTiled(tilesWidth: Int, tilesHeight: Int, world: World, offsetX: Float, offsetY: Float, builder: ArchetypeBuilder, tr: () -> String): GdxArray<Entity> {
        val entities = GdxArray<Entity>()
        for (x in 0 until tilesWidth)
            for (y in 0 until tilesHeight) {
                val tile = world.create(builder)
                val space = tile.space()
                val draw = tile.draw()
                val trName = tr.invoke()
                draw.texture = GGraphics.imgMan.tr(trName)
                draw.normal = GGraphics.imgMan.nor(trName)
                space.setPos(offsetX + x * draw.texture.regionWidth, offsetY + y * draw.texture.regionHeight)
                space.setDim(draw.texture.regionWidth.toFloat(), draw.texture.regionHeight.toFloat())
                draw.drawFront = {
                    DrawMethods.drawFront(space, draw, it)
                }
                draw.drawNormal = {
                    draw.angle = 0.0f
                    DrawMethods.drawNor(space, draw, it)
                }
                entities.add(tile)
            }
        return entities
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