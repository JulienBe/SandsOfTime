package be.particulitis.hourglass

import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import be.particulitis.hourglass.common.GBatch
import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.system.*
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera

/** First screen of the application. Displayed after the application is created.  */
class FirstScreen : Screen {

    override fun show() {
        val playerEntityId = world.create(Builder.player.build(world))
        Setup.player(playerEntityId, world)
        Gdx.input.inputProcessor = GInput
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
        val batch = GBatch(ImgMan())
        val config = WorldConfigurationBuilder()
                .with(SysControl())
                .with(SysCharMovement())
                .with(SysDirMovement())
                .with(SysShooter())

                .with(SysCollider())
                .with(SysDamage())
                .with(SysClampPos())
                .with(SysMap())

                .with(SysDrawer())

                .with(SysClearActions())
                .with(SysDead())
                .with(SysSpawner())
                .build()
        val world = World(config)
        val cam = OrthographicCamera(GResolution.screenWidth, GResolution.screenHeight)
    }
}