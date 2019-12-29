package be.particulitis.hourglass.states

import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.comp.CompHp
import be.particulitis.hourglass.entities
import be.particulitis.hourglass.screens.FirstScreen
import com.artemis.Aspect
import com.artemis.World
import kotlin.reflect.KClass

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

        GTime.reset()
        deleteAll(world, CompHp::class)
        FirstScreen.score = -1

        changedState(world)
    }

    fun deleteAll(world: World, comp: KClass<out Comp>): Int {
        val entities = world.entities(Aspect.all(comp.java))
        for (i in 0 until entities.size()) {
            world.delete(entities[i])
        }
        return entities.size()
    }

}