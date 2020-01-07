package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.gamedata.setups.SProps
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.system.SysDrawer
import be.particulitis.hourglass.system.SysTime
import be.particulitis.hourglass.system.ui.SysUiDisplay
import be.particulitis.hourglass.system.ui.SysUiPrettyDisplay
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Game

class TitleScreen(game: Game) : AbstractScreen(game) {

    private val config = WorldConfigurationBuilder()
            .with(SysTime())
            .with(SysDrawer())
            .with(SysUiPrettyDisplay())
            .with(SysUiDisplay())
            .build()
    val world = World(config)
    private val pointerLight = GLight.create(GResolution.screenWidth / 2f, GResolution.screenHeight / 2f, GPalette.WHITEISH, .8f)

    override fun show() {
        SUi.prettyDisplay(world, "Hourglass", 180f, 200f)
        SUi.button(world, "Play!", 20f, 50f) {
            game.screen = FirstScreen(game)
        }
        SProps.wall(world, 16, 16)
    }

    override fun render(delta: Float) {
        GLight.updatePos(pointerLight, GHelper.x, GHelper.y)
        world.setDelta(delta)
        GGraphics.render {
            world.process()
            super.render(delta)
        }
    }
}