package be.particulitis.hourglass

import com.badlogic.gdx.Game

class Hourglass : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}