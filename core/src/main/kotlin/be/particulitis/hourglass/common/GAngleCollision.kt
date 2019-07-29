package be.particulitis.hourglass.common

import com.badlogic.gdx.math.Vector2

class GAngleCollision(first: Float, second: Float, val side: GSide) {
    val max = Math.max(first, second)
    val min = Math.min(first, second)
    val diff = max - min

}