package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GSide
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.gamedata.Builder
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.World
import ktx.collections.GdxArray

object SProps : Setup() {
    fun ground(world: World, tilesHeight: Int, tilesWidth: Int) {
        createTiled(tilesWidth, tilesHeight, world, 0f, 0f, Builder.tiled) { "floor1" }
    }

    fun ground(world: World) {
        val tile = world.create(Builder.tiled)
        val space = tile.space()
        val draw = tile.draw()
        draw.drawFront = { batch, space ->
            batch.draw(GPalette.DARK_BLUE.tr, 0f, 0f, GResolution.areaW, GResolution.areaH)
        }
        draw.drawOcc = { batch, space ->
            batch.draw(GPalette.DARK_BLUE.img.occluder, 0f, 0f, GResolution.areaW, GResolution.areaH)
        }
        draw.drawNormal = { batch, space ->
            batch.draw(GPalette.DARK_BLUE.img.normal, 0f, 0f, GResolution.areaW, GResolution.areaH)
        }
        space.setPos(0f, 0f)
        space.setDim(GResolution.areaW, GResolution.areaH)
    }

    fun wall(world: World, tilesWidth: Int, tilesHeight: Int, exposedSide: GSide, offsetX: Float = 0f, offsetY: Float = 0f) {
        val walls = createTiled(tilesWidth, tilesHeight, world, offsetX, offsetY, Builder.wall) {
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
            draw.angle = exposedSide.angle + 90f
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

        space.setPos(x, y)
        draw.currentImg = GGraphics.img(name)
        draw.layer = 2
    }

    private fun createTiled(tilesWidth: Int, tilesHeight: Int, world: World, offsetX: Float, offsetY: Float, builder: ArchetypeBuilder, tr: () -> String): GdxArray<Entity> {
        val entities = GdxArray<Entity>()
        for (x in 0 until tilesWidth)
            for (y in 0 until tilesHeight) {
                val tile = world.create(builder)
                val space = tile.space()
                val draw = tile.draw()
                draw.currentImg = GGraphics.img(tr.invoke())
                space.setPos(offsetX + x * draw.currentImg.front.regionWidth, offsetY + y * draw.currentImg.front.regionHeight)
                space.setDim(draw.currentImg.front.regionWidth.toFloat(), draw.currentImg.front.regionHeight.toFloat())
                entities.add(tile)
            }
        return entities
    }

}