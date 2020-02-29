package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.gamedata.setups.SEnemy
import be.particulitis.hourglass.gamedata.setups.SPlayer
import be.particulitis.hourglass.gamedata.setups.SProps
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.graphics.SysBloomer
import be.particulitis.hourglass.system.graphics.SysDrawer
import be.particulitis.hourglass.system.graphics.SysUiDisplay
import be.particulitis.hourglass.system.graphics.SysUiPrettyAct
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.collections.GdxArray
import kotlin.math.cos
import kotlin.math.sin

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
            .with(SysHourglassDisplay())
            .with(SysClearActions())
            .build()
    val world = World(config)
    //private val pointerLight = GLight.create(GResolution.screenWidth / 2f, GResolution.screenHeight / 2f, GPalette.WHITEISH, .2f)
    //private val barrel = SProps.chest(world, 100f, 110f)
    private val lights = GdxArray<Int>()

    override fun show() {
        //lights.add(GLight.create(0f, 0f, GPalette.WHITEISH, .2f))
        //lights.add(GLight.create(0f, 0f, GPalette.WHITEISH, .2f))
        //lights.add(GLight.create(0f, 0f, GPalette.WHITEISH, .2f))
        Gdx.input.inputProcessor = GInput

        SUi.prettyDisplay(world, "Hourglass", 20f, 150f)
        SUi.button(world, "Play!", 0f, 0f) {
            switchScreen(GameScreen(game))
        }
//        SProps.ground(GameScreen.world, 18, 14)
        //SPlayer.player(world, 20f, 20f)
        //SPlayer.player(world, -20f, -20f)
        //SPlayer.player(world, -20f, 20f)
        //SPlayer.player(world, 20f, -20f)
        SPlayer.player(world)
        SEnemy.enemySlug(world, 30f, 30f)
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