package be.particulitis.hourglass.comp

import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.math.Vector2

class CompShooter : Comp() {

    var nextShoot = 0f
        private set
    var keyCheck = false
        private set
    var keyToCheck = 0
        private set
    var bullet = Pair(Builder.bullet, Setup::bullet)
        private set
    var offsetX = 0f
        private set
    var offsetY = 0f
        private set
    val dir = Vector2(1f, 0f)

    fun setKey(i: Int) {
        keyToCheck = i
        keyCheck = true
    }
}