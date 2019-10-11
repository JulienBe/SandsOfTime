package be.particulitis.hourglass.comp

import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import com.badlogic.gdx.math.Vector2

class CompShooter : Comp() {

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
    var firerate = .15f
        private set
    var nextShoot = 0f
    val iDir = Vector2(1f, 0f)

    var dir: (myPosX: Float, myPosY: Float) -> Vector2 = { x, y ->
        iDir.set(x, y)
    }

    fun setOffset(x: Float, y: Float) {
        offsetX = x
        offsetY = y
    }

    fun setKey(i: Int) {
        keyToCheck = i
        keyCheck = true
    }

    fun setShootingDir(shootingFunction: (x: Float, y: Float) -> Vector2) {
       this.dir = shootingFunction
    }
}