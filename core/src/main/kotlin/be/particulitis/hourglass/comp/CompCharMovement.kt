package be.particulitis.hourglass.comp

import com.artemis.PooledComponent

class CompCharMovement : PooledComponent() {

    var speed = 1f

    override fun reset() {
        speed = 1f
    }
}