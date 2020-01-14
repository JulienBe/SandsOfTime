package be.particulitis.hourglass.screens

import be.particulitis.hourglass.Boombox
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.comp.CompEnemy
import be.particulitis.hourglass.comp.CompLight
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.setups.SPlayer
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.states.StateManager
import be.particulitis.hourglass.system.*
import be.particulitis.hourglass.system.graphics.SysDrawer
import be.particulitis.hourglass.system.graphics.SysUiDisplay
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
class FirstScreen(game: Game) : AbstractScreen(game) {

    override fun show() {
        GTime.reset()
        SPlayer.player(world)
        SUi.score(world.create(Builder.score.build(world)), world)
        StateManager.endPause(world)
    }

    override fun render(delta: Float) {
        println("lights ${GLight.numberOfLights()}")
        world.setDelta(delta)
        GGraphics.render {
            GInput.newFrame()
            world.process()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.P))
            StateManager.invertPause(world)
    }

    companion object {
        var score = 0

        val config = WorldConfigurationBuilder()
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
                .with(SysClampPos())

                .with(SysLightTrack())
                .with(SysDrawer())
                .with(SysUiDisplay())

                .with(SysClearActions())
                .with(SysDead())
                .with(SysScore())
                .with(SysSpawner())
                .with(SysStartGame())
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
            world.aspectSubscriptionManager.get(Aspect.all(CompLight::class.java))
                    .addSubscriptionListener(object : EntitySubscription.SubscriptionListener {
                        val mLight = world.getMapper(CompLight::class.java)
                        override fun inserted(entities: IntBag?) { }
                        override fun removed(entities: IntBag) {
                            val ids: IntArray = entities.data
                            for (it in entities.size() - 1 downTo 0) {
                                mLight[ids[it]].clear()
                            }
                        }
                    })
        }

    }
}
