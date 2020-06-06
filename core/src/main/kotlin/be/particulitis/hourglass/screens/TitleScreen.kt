package be.particulitis.hourglass.screens

import be.particulitis.hourglass.Boombox
import be.particulitis.hourglass.button
import be.particulitis.hourglass.common.GHistoryFloat
import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.comp.ui.CompTxt
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.gamedata.setups.SUndertrail
import be.particulitis.hourglass.prettyUi
import be.particulitis.hourglass.space
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.graphics.*
import com.artemis.Entity
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx.*
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import ktx.collections.GdxArray
import kotlin.math.roundToInt

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

            .with(SysUndertrail())
            .with(SysDrawer())
            .with(SysUiPrettyAct())
            .with(SysUiControl())
            .with(SysUiDisplay())
            .with(SysClearActions())
            .build()
    val world = World(config)
    private var uppercaseIndex = 0
    private lateinit var supercomputer: CompPrettyUi
    private val supercomputerSources = arrayListOf(
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
    private val boombox = Boombox(world)
    private var backgroundParticles = GdxArray<BackgroundParticle>()
    private var txtArea = GdxArray<Pair<CompSpace, CompTxt>>()

    override fun show() {
        input.inputProcessor = GInput

        SUi.prettyDisplay(world, "8-BIT", 110f, 160f, 6)
        val supercomputerEntity = createTxt(SUi.prettyDisplay(world, "supercomputer", 40f, 120f, 6), false)
        supercomputer = supercomputerEntity.prettyUi()
        createTxt(supercomputerEntity, false)
        createTxt(SUi.button(world, "Play", 135f, 80f, 3) {
            switchScreen(GameScreen(game))
        }, true)
        createTxt(SUi.button(world, "Options", 120f, 55f, 3) {
            app.exit()
        }, true)
        createTxt(SUi.button(world, "Exit", 135f, 30f, 3) {
            app.exit()
        }, true)
        createTxt(SUi.prettyDisplay(world, "Please share this simulation with as many humans as possible", 50f, 2f), false)

        boombox.mTitleScreen.play()
        for (i in 0..10)
            backgroundParticles.add(BackgroundParticle())
    }

    private fun createTxt(entity: Entity, button: Boolean): Entity {
        txtArea.add(Pair(entity.space(), if (button) entity.button() else entity.prettyUi()))
        return entity
    }

    override fun render(delta: Float) {
        if (boombox.mTitleScreen.newBpm()) {
            backgroundParticles.forEach {
                it.changeDir()
                it.color = GPalette.rand()
                it.x.fill(it.x.get())
                it.y.fill(it.y.get())
            }
        }
        backgroundParticles.forEach { p ->
            p.move(boombox.mTitleScreen.currentHigh() / 140f)
            for (i in 0..p.x.historySize)
                SUndertrail.dot(p.x.get(i), p.y.get(i), p.color, world)
            txtArea.forEach {
                while (inRect(it, p)) {
                    p.dir.rotate90(GRand.oneOrMinus())
                    p.move(0.1f)
                    p.x.fill(p.x.get())
                    p.y.fill(p.y.get())
                }
            }
        }

        if (input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            uppercaseIndex++
            supercomputer.updateText(supercomputerSources[uppercaseIndex % supercomputerSources.size], 6)
        }
        world.setDelta(delta)
        GGraphics.render {
            world.process()
        }
    }

    private fun inRect(it: Pair<CompSpace, CompTxt>, p: BackgroundParticle) =
            Rectangle.tmp.set(it.second.x(it.first), it.second.y(it.first), it.second.displayW(), it.second.displayH()).contains(p.x.get(), p.y.get())
}

/**
 * Probably only there as a placeholder. I need to find something pretty for this background..
 */
data class BackgroundParticle(var x: GHistoryFloat = GHistoryFloat(GRand.randX(), 15), var y: GHistoryFloat = GHistoryFloat(GRand.randY(), 15), var dir: Vector2 = Vector2(0f, 1f), var color: GPalette = GPalette.rand()) {

    init {
        dir.nor()
        dir.rotate90(GRand.nextInt())
        dir.rotate90(GRand.nextInt())
    }

    fun move(currentHigh: Float) {
        val mvt = (currentHigh * graphics.deltaTime).roundToInt()
        for (i in 0..mvt)
            addToPos()
    }

    private fun addToPos() {
        x.add(MathUtils.clamp(x.get() + dir.x, 0f, GResolution.areaW - 1))
        y.add(MathUtils.clamp(y.get() + dir.y, 0f, GResolution.areaH - 1))
    }

    fun changeDir() {
        dir.rotate90(GRand.oneOrMinus())
    }

}