package be.particulitis.hourglass.screens

import be.particulitis.hourglass.font.FontPixel
import com.badlogic.gdx.Game

class Hourglass : Game() {
    override fun create() {
        // initialize it soon
        FontPixel
        setScreen(FirstScreen())
    }
}