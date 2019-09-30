package be.particulitis.hourglass

import be.particulitis.hourglass.builds.Aspects
import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import be.particulitis.hourglass.common.GBatch
import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GResolution
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
        Setup.player(world.create(Builder.player.build(world)), world)
        Setup.score(world.create(Builder.score.build(world)), world)
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

                .with(SysTtl())
                .with(SysCollider())
                .with(SysDamage())
                .with(SysClampPos())
                .with(SysMap())

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
        val subscription = world.aspectSubscriptionManager.get(Aspect.all(Aspects.Enemy.comps))
                .addSubscriptionListener(object : EntitySubscription.SubscriptionListener {
                    override fun inserted(entities: IntBag) {
                    }
                    override fun removed(entities: IntBag) {
                        score += entities.size()
                    }
                })

    }
}
