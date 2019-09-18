package be.particulitis.hourglass.states

import com.artemis.World

object StateManager {

    private var currentState = StateSystems.RUNNING

    fun pause(world: World) {
        currentState = StateSystems.PAUSED
        changedState(world)
    }

    fun endPause(world: World) {
        currentState = StateSystems.RUNNING
        changedState(world)
    }

    fun invertPause(world: World) {
        when (currentState) {
            StateSystems.RUNNING -> pause(world)
            StateSystems.PAUSED -> endPause(world)
        }
    }

    private fun changedState(world: World) {
        currentState.systems.forEach {
            world.getSystem(it.first).isEnabled = it.second
        }
    }

    fun playerDead(world: World) {
        currentState = StateSystems.PLAYER_DEAD
        changedState(world)
    }

}