package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GGraphics
import be.particulitis.hourglass.common.GResolution
import com.badlogic.gdx.ScreenAdapter

abstract class AbstractScreen : ScreenAdapter() {

    override fun resize(width: Int, height: Int) {
        GResolution.compute()
        GGraphics.cam.viewportWidth = GResolution.screenWidth
        GGraphics.cam.viewportHeight = GResolution.screenHeight
        GGraphics.cam.position.set(GGraphics.cam.viewportWidth / 2f, GGraphics.cam.viewportHeight / 2f, 0f)
    }

}