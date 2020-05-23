package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.gamedata.setups.SProps
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.prettyUi
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.graphics.*
import com.artemis.Entity
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils

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
            .with(SysUiPrettyAct())
            .with(SysUiControl())
            .with(SysUiDisplay())
            .with(SysClearActions())
            .build()
    val world = World(config)
    val light = GLight(0.1f, 60f, 1.0f, 0f, 1.1f, 1f, 0.2f, 0.2f)
    var previousX = 0f
    var previousY = 0f
    var lightIntensity = 0.1f
    var colorIndex: Int = GRand.int(0, 16)
    var uppercaseIndex = 0
    lateinit var supercomputer: CompPrettyUi
    val supercomputerSources = arrayListOf(
            "supercomputer",
            "Supercomputer",
            "sUpercomputer",
            "suPercomputer",
            "supErcomputer",
            "supeRcomputer",
            "superComputer",
            "supercOmputer",
            "supercoMputer",
            "supercomPuter",
            "supercompUter",
            "supercompuTer",
            "supercomputEr",
            "supercomputeR"
    )

    override fun show() {
        Gdx.input.inputProcessor = GInput

        SUi.prettyDisplay(world, "8-BIT", 110f, 160f, 6)
        supercomputer = SUi.prettyDisplay(world, "supercomputer", 40f, 120f, 6).prettyUi()
        SUi.button(world, "Play", 135f, 80f, 3) {
            switchScreen(GameScreen(game))
        }
        SUi.button(world, "Options", 120f, 60f, 3) {
            Gdx.app.exit()
        }
        SUi.button(world, "Exit", 135f, 40f, 3) {
            Gdx.app.exit()
        }
        SProps.ground(world)
    }

    override fun render(delta: Float) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            uppercaseIndex++
            supercomputer.updateText(supercomputerSources[uppercaseIndex % supercomputerSources.size], 6)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            switchScreen(GameScreen(game))
        }
        light.updatePosAngle(GHelper.x, GHelper.y, GTime.time * 180f)
        if (previousX != GHelper.x || previousY != GHelper.y) {
            lightIntensity += 0.0002f
            lightIntensity *= 1.1f
        } else {
            lightIntensity -= 0.0015f
        }
        previousX = GHelper.x
        previousY = GHelper.y
        lightIntensity = MathUtils.clamp(lightIntensity, 0f, 0.15f)
        val lightColor = GPalette.values()[colorIndex % GPalette.values().size]
        light.updateIntesityRGB(lightIntensity, lightColor.r, lightColor.g, lightColor.b)
        if (Gdx.input.justTouched()) {
            colorIndex++
        }
        world.setDelta(delta)
        GGraphics.render {
            world.process()
            super.render(delta)
        }
    }
}