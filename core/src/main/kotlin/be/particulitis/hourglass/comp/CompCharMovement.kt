package be.particulitis.hourglass.comp

import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

class CompCharMovement : Comp() {

    var speed = 1f
    var vec = Vector2()

    override fun reset() {
        speed = 1f
        vec.set(0f, 0f)
    }
}