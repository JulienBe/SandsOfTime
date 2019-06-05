package be.particulitis.hourglass

import be.particulitis.hourglass.archetypes.Builder
import be.particulitis.hourglass.common.GAction
import be.particulitis.hourglass.common.GBatch
import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.comp.CompControl
import be.particulitis.hourglass.system.*
import com.artemis.World
import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen

/** First screen of the application. Displayed after the application is created.  */
class FirstScreen : Screen {

    override fun show() {
        val playerEntityId = world.create(Builder.player.build(world))
        val playerControl = world.getEntity(playerEntityId).getComponent(CompControl::class.java)
        playerControl.addAction(listOf(Input.Keys.Q, Input.Keys.A, Input.Keys.LEFT), GAction.LEFT)
        playerControl.addAction(listOf(Input.Keys.D, Input.Keys.RIGHT), GAction.RIGHT)
        playerControl.addAction(listOf(Input.Keys.Z, Input.Keys.W, Input.Keys.UP), GAction.UP)
        playerControl.addAction(listOf(Input.Keys.S, Input.Keys.DOWN), GAction.DOWN)

        Gdx.input.inputProcessor = GInput
    }

    override fun render(delta: Float) {
        GInput.newFrame()
        world.setDelta(delta)
        batch.begin()
        world.process()
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        // Resize your screen here. The parameters represent the new window size.
    }

    override fun pause() {
        // Invoked when your application is paused.
    }

    override fun resume() {
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {
        // This method is called when another screen replaces this one.
    }

    override fun dispose() {
        // Destroy screen's assets here.
    }

    companion object {
        val batch = GBatch(ImgMan())
        val config = WorldConfigurationBuilder()
                .with(SysControl())
                .with(SysCharMovement())
                .with(SysCollider())
                .with(SysDrawer())
                .with(SysClearActions())
                .build()
        val world = World(config)
    }
}