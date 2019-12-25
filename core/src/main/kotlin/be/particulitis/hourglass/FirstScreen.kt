package be.particulitis.hourglass

import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.comp.CompEnemy
import be.particulitis.hourglass.comp.CompLight
import be.particulitis.hourglass.gamedata.setups.SPlayer
import be.particulitis.hourglass.gamedata.setups.SUi
import be.particulitis.hourglass.states.StateManager
import be.particulitis.hourglass.system.*
import com.artemis.Aspect
import com.artemis.EntitySubscription
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.artemis.managers.TagManager
import com.artemis.utils.IntBag
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera

/** First screen of the application. Displayed after the application is created.  */
class FirstScreen : Screen {

    override fun show() {
        GTime.reset()
        SPlayer.player(world.create(Builder.player.build(world)), world)
        SUi.score(world.create(Builder.score.build(world)), world)
        Gdx.input.inputProcessor = GInput
        StateManager.endPause(world)
    }

    override fun render(delta: Float) {
        cls()
        cam.update()
        batch.projectionMatrix = cam.combined
        GInput.newFrame()
        world.setDelta(delta)
        batch.begin()
        world.process()
        batch.end()
        if (Gdx.input.isKeyJustPressed(Input.Keys.P))
            StateManager.invertPause(world)
    }

    private fun cls() {
        Gdx.graphics.gL20.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {
        GResolution.compute()
        cam.viewportWidth = GResolution.screenWidth
        cam.viewportHeight = GResolution.screenHeight
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0f)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun hide() {
    }

    override fun dispose() {
    }

    companion object {
        var score = 0
        val batch = GBatch(ImgMan())
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
        val cam = OrthographicCamera(GResolution.screenWidth, GResolution.screenHeight)
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
