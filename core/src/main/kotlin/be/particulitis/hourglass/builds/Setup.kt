package be.particulitis.hourglass.builds

import be.particulitis.hourglass.common.GAction
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.comp.CompCollide
import be.particulitis.hourglass.comp.CompControl
import be.particulitis.hourglass.comp.CompDimension
import com.artemis.World
import com.badlogic.gdx.Input

object Setup {

    const val enemyDim = 10f
    const val playerDim = 5f
    const val playerHDim = playerDim / 2f

    fun player(playerEntityId: Int, world: World) {
        val playerControl = world.getEntity(playerEntityId).getComponent(CompControl::class.java)
        playerControl.addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        playerControl.addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        playerControl.addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        playerControl.addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        dim(playerEntityId, world, GResolution.areaHDim - playerHDim, GResolution.areaHDim - playerHDim, playerDim, playerDim)
    }

    fun enemy(id: Int, world: World) {
        dim(id, world, GRand.float(0f, GResolution.areaDim - enemyDim), GRand.float(0f, GResolution.areaDim - enemyDim), enemyDim, enemyDim)
    }

    private fun dim(id: Int, world: World, x: Float, y: Float, w: Float, h: Float) {
        val dim = world.getEntity(id).getComponent(CompDimension::class.java)
        dim.setDim(10f, 10f)
        dim.setPos(x, y)
    }
}