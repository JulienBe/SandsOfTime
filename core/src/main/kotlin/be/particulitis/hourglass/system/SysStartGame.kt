package be.particulitis.hourglass.system

import be.particulitis.hourglass.gamedata.setups.SPlayer
import be.particulitis.hourglass.states.StateManager
import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class SysStartGame : BaseSystem() {

    override fun processSystem() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            SPlayer.player(world)
            StateManager.endPause(world)
        }
    }

}