package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import be.particulitis.hourglass.comp.CompHp
import be.particulitis.hourglass.states.StateManager
import com.artemis.Aspect
import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class SysStartGame : BaseSystem() {

    override fun processSystem() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            val playerEntityId = FirstScreen.world.create(Builder.player.build(FirstScreen.world))
            Setup.player(playerEntityId, FirstScreen.world)
            StateManager.endPause(world)
        }
    }

}