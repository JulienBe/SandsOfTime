package be.particulitis.hourglass.comp

import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import com.artemis.ArchetypeBuilder
import com.artemis.World
import com.badlogic.gdx.math.Vector2
import kotlin.reflect.KFunction5

class CompShooter : Comp() {

    var keyCheck = false
        private set
    var keyToCheck = 0
        private set
    var bullet = Pair(Builder.bullet, Setup::playerBullet)
        private set
    var offsetX = 0f
        private set
    var offsetY = 0f
        private set
    var firerate = .15f
        private set
    var nextShoot = 0f
    val dir = Vector2(1f, 0f)
    var shouldShood = { true }
    var shootingFunc = {}

    fun setBullet(build: ArchetypeBuilder, setup: KFunction5<@ParameterName(name = "id") Int, @ParameterName(name = "world") World, @ParameterName(name = "posX") Float, @ParameterName(name = "posY") Float, @ParameterName(name = "dir") Vector2, Unit>) {
        bullet = Pair(build, setup)
    }

    fun setOffset(x: Float, y: Float) {
        offsetX = x
        offsetY = y
    }

    fun setKey(i: Int) {
        keyToCheck = i
        keyCheck = true
    }

    fun setFirerate(firerate: Float) {
        this.firerate = firerate
    }


    override fun reset() {
        super.reset()
        keyCheck = false
        keyToCheck = 0
        offsetX = 0f
        offsetY = 0f
        firerate = .15f
        nextShoot = 0f
    }
}

enum class ShootingTypes {

}