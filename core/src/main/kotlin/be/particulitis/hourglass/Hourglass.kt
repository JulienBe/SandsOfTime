package be.particulitis.hourglass

import be.particulitis.hourglass.font.FontPixel
import be.particulitis.hourglass.screens.TitleScreen
import com.badlogic.gdx.Game

class Hourglass : Game() {
    override fun create() {
        // initialize it soon
        FontPixel
        //setScreen(FirstScreen())
        setScreen(TitleScreen(this))
    }
}