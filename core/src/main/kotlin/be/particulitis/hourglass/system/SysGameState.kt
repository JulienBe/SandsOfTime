package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.Comp
import be.particulitis.hourglass.comp.CompHp
import be.particulitis.hourglass.entities
import be.particulitis.hourglass.gamedata.setups.SPlayer
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.screens.GameScreen
import be.particulitis.hourglass.states.StateManager
import be.particulitis.hourglass.states.StateSystems
import com.artemis.Aspect
import com.artemis.BaseSystem
import com.artemis.Entity
import com.artemis.World
import kotlin.reflect.KClass

class SysGameState : BaseSystem() {

    var previousState = StateSystems.NONE
    var playButton: Entity? = null

    override fun processSystem() {
        println("SysGameState.processSystem")
        if (StateManager.currentState != previousState)
            onNewState()
        previousState = StateManager.currentState

    }

    private fun onNewState() {
        when (StateManager.currentState) {
            StateSystems.WAITING_START -> {
                playButton = SUi.button(world, "Play", 105f, 80f, 2, true) {
                    SPlayer.player(world)
                    StateManager.endPause(world)
                }
            }
            StateSystems.RUNNING -> {
                playButton?.deleteFromWorld()
            }
            StateSystems.PLAYER_DEAD -> {
                GTime.reset()
                deleteAll(world, CompHp::class)
                GameScreen.score = -1
            }
            else -> {}
        }
    }

    private fun deleteAll(world: World, comp: KClass<out Comp>): Int {
        val entities = world.entities(Aspect.all(comp.java))
        for (i in 0 until entities.size()) {
            world.delete(entities[i])
        }
        return entities.size()
    }

}