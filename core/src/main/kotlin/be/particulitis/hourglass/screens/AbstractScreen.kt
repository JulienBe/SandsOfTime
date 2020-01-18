package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GResolution
import com.badlogic.gdx.Game
import com.badlogic.gdx.ScreenAdapter

abstract class AbstractScreen(val game: Game) : ScreenAdapter() {

    override fun resize(width: Int, height: Int) {
        GResolution.compute()
        GGraphics.cam.viewportWidth = GResolution.screenWidth
        GGraphics.cam.viewportHeight = GResolution.screenHeight
        GGraphics.cam.position.set(GGraphics.cam.viewportWidth / 2f, GGraphics.cam.viewportHeight / 2f, 0f)
    }

    fun switchScreen(screen: AbstractScreen) {
        GLight.clear()
        game.screen = screen
    }

}