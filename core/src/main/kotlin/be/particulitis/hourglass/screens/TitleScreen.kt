package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.setups.*
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.graphics.SysBloomer
import be.particulitis.hourglass.system.graphics.SysDrawer
import be.particulitis.hourglass.system.graphics.SysHourglassDisplay
import be.particulitis.hourglass.system.graphics.SysHpDisplay
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

class TitleScreen(game: Game) : AbstractScreen(game) {

    private val config = WorldConfigurationBuilder()
            .with(TagManager())
            .with(SysTime())

            .with(SysControl())
            .with(SysCharMovement())
            .with(SysDirMovement())
            .with(SysShooter())
            .with(SysParticle())
            .with(SysTtl())

            .with(SysDrawer())
            .with(SysBloomer())
            .with(SysHpDisplay())
            .with(SysHourglassDisplay())
            .with(SysClearActions())
            .build()
    val world = World(config)

    override fun show() {
        Gdx.input.inputProcessor = GInput

//        SUi.prettyDisplay(world, "Hourglass", 20f, 150f)
//        SUi.button(world, "Play!", 0f, 0f) {
//            switchScreen(GameScreen(game))
//        }
        SPlayer.player(world)
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            switchScreen(GameScreen(game))
        }

        world.setDelta(delta)
        GGraphics.render {
            world.process()
            super.render(delta)
        }
    }
}