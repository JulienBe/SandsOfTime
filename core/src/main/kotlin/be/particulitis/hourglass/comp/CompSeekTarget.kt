package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.GAction
import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

class CompSeekTarget : Comp() {
    val target = Vector2()

    val x get() = target.x
    val y get() = target.y

    fun set(target: Vector2) {
        this.target.set(target)
    }

    fun clear() {
        reset()
    }
}