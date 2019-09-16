package be.particulitis.hourglass.comp

import com.badlogic.gdx.math.Vector2

class CompIsPlayer : Comp() {

    var isPlayer = true
        private set

    fun setPlayer(isPlayer: Boolean) {
        this.isPlayer = isPlayer
    }

    override fun reset() {
        super.reset()
        isPlayer = true
    }

}