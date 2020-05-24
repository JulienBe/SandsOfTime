package be.particulitis.hourglass.screens

import be.particulitis.hourglass.Boombox
import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GKeyGlobalState
import be.particulitis.hourglass.common.GSide
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.CompEnemy
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.setups.SProps
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.states.StateManager
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.graphics.*
import com.artemis.Aspect
import com.artemis.EntitySubscription
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.artemis.utils.IntBag
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

/** First screen of the application. Displayed after the application is created.  */
class GameScreen(game: Game) : AbstractScreen(game) {

    override fun show() {
        GTime.reset()
        SUi.score(world.create(Builder.score.build(world)), world)
    }

    override fun render(delta: Float) {
        world.setDelta(delta)
        GGraphics.render {
            GInput.newFrame()
            GKeyGlobalState.act()
            world.process()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P))
            StateManager.invertPause(world)
    }

    companion object {
        var score = 0

        private val config = WorldConfigurationBuilder()
                .with(SysGameState())
                .with(TagManager())
                .with(SysTime())
                .with(SysControl())
                .with(SysCharMovement())
                .with(SysTargetAcquisition())
                .with(SysTargetSeek())
                .with(SysDirMovement())
                .with(SysShooter())
                .with(SysParticle())

                .with(SysTtl())
                .with(SysCollider())
                .with(SysDamage())

                .with(SysAct())
                .with(SysDrawer())
                .with(SysBloomer())
                .with(SysUiDisplay())
                .with(SysUiPrettyAct())
                .with(SysUiControl())
                .with(SysHpDisplay())

                .with(SysClearActions())
                .with(SysDead())
                .with(SysSpawner())
                .build()
        val world = World(config)
        val boombox = Boombox(world)

        init {
            world.aspectSubscriptionManager.get(Aspect.all(CompEnemy::class.java))
                    .addSubscriptionListener(object : EntitySubscription.SubscriptionListener {
                        override fun inserted(entities: IntBag) { }
                        override fun removed(entities: IntBag) {
                            score++
                        }
                    })
            stageSetup()
            StateManager.waitingStart(world)
            boombox.play(boombox.mLevel1)
        }

        private fun stageSetup() {
            SProps.ground(world, 14, 18)

            SProps.wall(world, (GResolution.areaW / Dim.WallSprite.w).toInt() + 1, 1, GSide.BOTTOM, 0f, GResolution.areaH - Dim.WallSprite.h + 7f)
            SProps.wall(world, (GResolution.areaW / Dim.WallSprite.w).toInt() + 1, 1, GSide.TOP, 0f, -7f)
            SProps.wall(world, 1,  (GResolution.areaH / Dim.WallSprite.h).toInt() + 1, GSide.LEFT, GResolution.areaW - Dim.WallSprite.h, 0f)
            SProps.wall(world, 1,  (GResolution.areaH / Dim.WallSprite.h).toInt() + 1, GSide.RIGHT, -Dim.WallSprite.hh - 3f, 0f)
        }

    }
}
