package be.particulitis.hourglass.comp

import be.particulitis.hourglass.builds.Colors
import com.artemis.PooledComponent

open class Comp : PooledComponent() {

    var generation = 0
        private set

    override fun reset() {
        generation++
    }
}
class CompScore : Comp()
class CompDraw : Comp() {
    var color = Colors.scoreFont
}
class CompEnemy : Comp()