package be.particulitis.hourglass.comp

import com.badlogic.gdx.math.Vector2

class CompDir : Comp() {

    var maxSpeed = 100f
        private set
    var maxAcceleration = 10f
        private set
    var dir = Vector2()

    val x get() = dir.x
    val y get() = dir.y

     fun setSpeedAcceleration(maxSpeed: Float, maxAcceleration: Float) {
        this.maxSpeed = maxSpeed
        this.maxAcceleration = maxAcceleration
    }

    fun set(dir: Vector2) {
        this.dir.set(dir)
    }

    fun add(x: Float, y: Float) {
        dir.add(x, y)
    }

    // TODO: enrich vec2 to avoid going over max speed
    fun clamp() {
        dir.clamp(-maxSpeed, maxSpeed)
    }

    override fun reset() {
        super.reset()
        maxSpeed = 200f
        maxAcceleration = 20f
        dir.set(0f, 0f)
    }

}