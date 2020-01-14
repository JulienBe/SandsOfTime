package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.gamedata.setups.SPlayer
import be.particulitis.hourglass.gamedata.setups.SProps
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.system.graphics.SysDrawer
import be.particulitis.hourglass.system.SysTime
import be.particulitis.hourglass.system.graphics.SysUiDisplay
import be.particulitis.hourglass.system.graphics.SysUiPrettyAct
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx

class TitleScreen(game: Game) : AbstractScreen(game) {

    private val config = WorldConfigurationBuilder()
            .with(TagManager())
            .with(SysTime())
            .with(SysDrawer())
            .with(SysUiPrettyAct())
            .with(SysUiDisplay())
            .build()
    val world = World(config)
    private val pointerLight = GLight.create(GResolution.screenWidth / 2f, GResolution.screenHeight / 2f, GPalette.WHITEISH, .5f)

    override fun show() {
        SUi.prettyDisplay(world, "Hourglass", 20f, 150f)
//        SUi.button(world, "Play!", 20f, 50f) {
//            game.screen = FirstScreen(game)
//        }
        SProps.wall(world, 16, 16)
        SPlayer.player(world)
    }

    override fun render(delta: Float) {
        if (Gdx.input.justTouched()) {
            SProps.dent(world, 2, GHelper.x, GHelper.y)
        }
        GLight.updatePos(pointerLight, GHelper.x, GHelper.y)
        world.setDelta(delta)
        GGraphics.render {
            world.process()
            super.render(delta)
        }
    }
}