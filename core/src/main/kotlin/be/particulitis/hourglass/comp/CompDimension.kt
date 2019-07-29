package be.particulitis.hourglass.comp

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

class CompDimension : Comp() {
    private val pos = Vector2()
    var w: Float = 1f
        private set
    var hw = w / 2f
        private set
    var h: Float = 1f
        private set
    var hh = h / 2f
        private set
    var z: Float = 0f
        private set
    val x get() = pos.x
    val y get() = pos.y
    val rect = Rectangle()

    override fun reset() {
        pos.x = 0f
        pos.y = 0f
    }

    fun setDim(w: Float, h: Float) {
        this.w = w
        this.h = h
        hw = w / 2f
        hh = h / 2f
        rect.setSize(w, h)
    }

    fun move(x: Float, y: Float) {
        setPos(pos.x + x, pos.y + y)
    }

    fun setPos(x: Float, y: Float) {
        pos.set(x, y)
        rect.setPosition(pos)
    }
}