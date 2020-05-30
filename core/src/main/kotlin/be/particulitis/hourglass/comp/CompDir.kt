package be.particulitis.hourglass.comp

import com.badlogic.gdx.math.Vector2

class CompDir : Comp() {

    var maxSpeed = 100f
        private set
    var maxAcceleration = 10f
        private set
    var v = Vector2()

    val x get() = v.x
    val y get() = v.y
    val angle get() = v.angle()

     fun setSpeedAcceleration(maxSpeed: Float, maxAcceleration: Float) {
        this.maxSpeed = maxSpeed
        this.maxAcceleration = maxAcceleration
    }

    fun set(dir: Vector2) {
        set(dir.x, dir.y)
    }

    fun set(x: Float, y: Float) {
        this.v.set(x, y)
    }

    fun add(x: Float, y: Float) {
        v.add(x, y)
    }

    fun mul(mul: Float) {
        v.scl(mul)
    }

    fun setAngle(angle: Float) {
        v.setAngle(angle)
    }

    // TODO: enrich vec2 to avoid going over max speed
    fun clamp() {
        v.clamp(-maxSpeed, maxSpeed)
    }

    override fun reset() {
        super.reset()
        maxSpeed = 100f
        maxAcceleration = 10f
        v.set(0f, 0f)
        println("$this : $generation")
    }

    fun rotate(angle: Float) {
        v.rotate(angle)
    }

}