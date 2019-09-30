package be.particulitis.hourglass

import com.badlogic.gdx.Game

class Hourglass : Game() {
    override fun create() {
        // initialize it soon
        FontPixel
        setScreen(FirstScreen())
    }
}