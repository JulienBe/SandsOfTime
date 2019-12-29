package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.ui.SysUiPrettyDisplay
import com.artemis.World
import com.artemis.WorldConfigurationBuilder

class TitleScreen : AbstractScreen() {

    val config = WorldConfigurationBuilder()
            .with(SysTime())
            .with(SysUiPrettyDisplay())
            .build()
    val world = World(config)

    override fun show() {
        SUi.prettyDisplay(world.create(Builder.prettyDisplay.build(world)), world, "Hourglass", 180f, 200f)
    }

    override fun render(delta: Float) {
        world.setDelta(delta)
        GGraphics.render {
            world.process()
            super.render(delta)
        }
    }
}