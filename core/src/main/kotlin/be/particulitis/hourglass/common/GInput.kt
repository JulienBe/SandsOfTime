package be.particulitis.hourglass.common

import com.badlogic.gdx.InputAdapter
import ktx.collections.gdxArrayOf

object GInput : InputAdapter() {

    private val keyJustPressed = gdxArrayOf<Int>()
    private val keyPressed = gdxArrayOf<Int>()

    override fun keyDown(keycode: Int): Boolean {
        keyJustPressed.add(keycode)
        keyPressed.add(keycode)
        return super.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        keyPressed.removeValue(keycode, true)
        return super.keyUp(keycode)
    }

    fun isKeyPressed(keycode: Int): Boolean {
        return keyPressed.contains(keycode, true)
    }

    fun newFrame() {
        keyJustPressed.clear()
    }

    fun isKeyJustPressed(key: Int): Boolean {
        return keyJustPressed.contains(key)
    }
}