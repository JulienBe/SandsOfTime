package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Phases
import com.artemis.World
import com.badlogic.gdx.math.Vector2

object SSpawns : Setup() {

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
            SEnemy.enemySlug(world, spawnX, spawnY)
        }
    }

}