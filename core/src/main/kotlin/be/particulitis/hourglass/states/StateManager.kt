package be.particulitis.hourglass.states

import com.artemis.World

object StateManager {

    var currentState = StateSystems.RUNNING

    fun waitingStart(world: World) {
        changedState(world, StateSystems.WAITING_START)
    }

    fun pause(world: World) {
        changedState(world, StateSystems.PAUSED)
    }

    fun endPause(world: World) {
        changedState(world, StateSystems.RUNNING)
    }

    fun playerDead(world: World) {
        changedState(world, StateSystems.PLAYER_DEAD)
    }

    fun invertPause(world: World) {
        when (currentState) {
            StateSystems.RUNNING -> pause(world)
            StateSystems.PAUSED -> endPause(world)
        }
    }

    private fun changedState(world: World, new: StateSystems) {
        currentState = new

        currentState.systems.forEach {
            world.getSystem(it.first).isEnabled = it.second
        }
    }

}