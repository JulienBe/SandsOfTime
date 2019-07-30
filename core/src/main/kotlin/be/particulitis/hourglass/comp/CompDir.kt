package be.particulitis.hourglass.comp

import com.badlogic.gdx.math.Vector2

class CompDir : Comp() {
    var dir = Vector2()

    val x get() = dir.x
    val y get() = dir.y

    fun set(dir: Vector2) {
        this.dir.set(dir)
    }
}