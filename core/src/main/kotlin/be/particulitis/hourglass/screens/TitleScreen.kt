package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.gamedata.setups.SParticles
import be.particulitis.hourglass.gamedata.setups.SPlayer
import be.particulitis.hourglass.gamedata.setups.SProps
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.graphics.SysDrawer
import be.particulitis.hourglass.system.graphics.SysUiDisplay
import be.particulitis.hourglass.system.graphics.SysUiPrettyAct
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
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
            .with(SysUiPrettyAct())
            .with(SysUiDisplay())
            .with(SysClearActions())
            .build()
    val world = World(config)
    //private val pointerLight = GLight.create(GResolution.screenWidth / 2f, GResolution.screenHeight / 2f, GPalette.WHITEISH, .1f)
    //private val barrel = SProps.chest(world, 100f, 110f)
    private val lights = GdxArray<Int>()

    override fun show() {
        lights.add(GLight.create(0f, 0f, GPalette.WHITEISH, .2f))
        //lights.add(GLight.create(0f, 0f, GPalette.WHITEISH, .2f))
        //lights.add(GLight.create(0f, 0f, GPalette.WHITEISH, .2f))
        Gdx.input.inputProcessor = GInput

//        SUi.prettyDisplay(world, "Hourglass", 20f, 150f)
//        SUi.button(world, "Play!", 20f, 50f) {
//            switchScreen(FirstScreen(game))
//        }
        SProps.wall(world, 16, 16)
        SPlayer.player(world)
        SPlayer.player(world, 20f, 20f)
        SPlayer.player(world, -20f, -20f)
        SPlayer.player(world, -20f, 20f)
        SPlayer.player(world, 20f, -20f)
    }

    override fun render(delta: Float) {
        if (Gdx.input.justTouched()) {
            for (i in 0..50)
                SParticles.explosionParticle(world, GHelper.x, GHelper.y, 38f)
        }
        //GLight.updatePos(pointerLight, GHelper.x, GHelper.y)
        lights.forEachIndexed { index, i ->
            GLight.updatePos(i, 128f + sin(GTime.time + index * 180f) * 70f, 128f + cos(GTime.time + index * 180f) * 50f)
        }

        world.setDelta(delta)
        GGraphics.render {
            world.process()
            super.render(delta)
        }
    }
}