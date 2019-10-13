package be.particulitis.hourglass.comp

import com.artemis.PooledComponent

open class Comp : PooledComponent() {

    var generation = 0
        private set

    override fun reset() {
        generation++
    }
}
class CompScore : Comp()

class CompEnemy : Comp()