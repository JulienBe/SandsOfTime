package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.ui.SysUiDisplay
import be.particulitis.hourglass.system.ui.SysUiPrettyDisplay
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Game

class TitleScreen(game: Game) : AbstractScreen(game) {

    val config = WorldConfigurationBuilder()
            .with(SysTime())
            .with(SysUiPrettyDisplay())
            .with(SysUiDisplay())
            .build()
    val world = World(config)

    override fun show() {
        SUi.prettyDisplay(world.create(Builder.prettyDisplay.build(world)), world, "Hourglass", 180f, 200f)
        SUi.button(world.create(Builder.button.build(world)), world, "Play!", 20f, 50f) {
            game.screen = FirstScreen(game)
        }
    }

    override fun render(delta: Float) {
        world.setDelta(delta)
        GGraphics.render {
            world.process()
            super.render(delta)
        }
    }
}