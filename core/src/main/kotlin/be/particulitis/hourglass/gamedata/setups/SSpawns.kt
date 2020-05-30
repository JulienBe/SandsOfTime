package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.create
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Phases
import be.particulitis.hourglass.layer
import be.particulitis.hourglass.space
import be.particulitis.hourglass.ttl
import com.artemis.World
import com.badlogic.gdx.math.Vector2

object SSpawns {

    private val cpuOffset = Array(9 * 9) { i ->
        Vector2(((i / 9) - 4).toFloat(), ((i % 9) - 4).toFloat())
    }
    fun cpuSpawn(world: World, x: Float, y: Float) {
        val s = world.create(Builder.beacon)
        s.layer().setLayer(Phases.Other)
        s.space().setPos(x, y)
        cpuOffset.forEach {
            SParticles.spawnAnim(world, x + it.x, y + it.y)
        }
        s.ttl().remaining = 1.4f
        s.ttl().onEnd = {
            val spawnX = x - 6
            val spawnY = y - 6
//            SCentralCrabUnit.setup(world, spawnX, spawnY)
            SGunner.setup(world, spawnX, spawnY)
        }
    }

}