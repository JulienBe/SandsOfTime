package be.particulitis.hourglass.comp

import be.particulitis.hourglass.common.GAction
import com.artemis.PooledComponent
import com.badlogic.gdx.math.Vector2

class CompTargetFollow : Comp() {
    lateinit var target: CompSpace
        private set
    var gen = 0
        private set

    fun set(target: CompSpace) {
        this.target = target
        gen = target.generation
    }

    fun clear() {
        reset()
    }
}