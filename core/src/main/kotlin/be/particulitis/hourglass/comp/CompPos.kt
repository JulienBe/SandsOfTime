package be.particulitis.hourglass.comp

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

class CompPos : PooledComponent() {
    val pos = Vector2()
    var z: Float = 0f
    var w: Float = 1f
    var h: Float = 1f
    var hw = w / 2f
    var hh = h / 2f
    val x get() = pos.x
    val y get() = pos.y

    override fun reset() {
        pos.x = 0f
        pos.y = 0f
    }

    fun setDim(w: Float, h: Float) {
        this.w = w
        this.h = h
        hw = w / 2f
        hh = h / 2f
    }
}