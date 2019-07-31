package be.particulitis.hourglass.builds

import be.particulitis.hourglass.common.GAction
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.comp.*
import com.artemis.World
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2

object Setup {

    const val enemyDim = 10f
    const val playerDim = 5f
    const val playerHDim = playerDim / 2f

    fun player(playerEntityId: Int, world: World) {
        val player = world.getEntity(playerEntityId)
        val playerControl = player.getComponent(CompControl::class.java)
        playerControl.addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        playerControl.addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        playerControl.addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        playerControl.addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        player.getComponent(CompHp::class.java).setHp(10)
        dim(playerEntityId, world, GResolution.areaHDim - playerHDim, GResolution.areaHDim - playerHDim, playerDim, playerDim)

        player.getComponent(CompShooter::class.java).setKey(Input.Keys.SPACE)
    }

    fun enemy(id: Int, world: World) {
        dim(id, world, GRand.float(0f, GResolution.areaDim - enemyDim), GRand.float(0f, GResolution.areaDim - enemyDim), enemyDim, enemyDim)
    }

    fun bullet(id: Int, world: World, posX: Float, posY: Float, dir: Vector2) {
        dim(id, world, posX, posY, 1f, 1f)
        val bullet = world.getEntity(id)
        bullet.getComponent(CompDir::class.java).set(dir)
        bullet.getComponent(CompHp::class.java).setHp(1)
    }

    private fun dim(id: Int, world: World, x: Float, y: Float, w: Float, h: Float) {
        val dim = world.getEntity(id).getComponent(CompSpace::class.java)
        dim.setDim(10f, 10f)
        dim.setPos(x, y)
    }
}