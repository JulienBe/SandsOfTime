package be.particulitis.hourglass.common

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import kotlin.math.roundToInt

enum class GDir {
    UpLeft, Up, UpRight,
    Left, None, Right,
    DownLeft, Down, DownRight;

    companion object {
        fun get(xAxis: Float, yAxis: Float): GDir {
            val x = MathUtils.clamp(xAxis.roundToInt(), -1, 1) + 1
            val y = (MathUtils.clamp(yAxis.roundToInt(), -1, 1) + 1) * 3
            return values()[x + y]
        }
        fun get(vector2: Vector2): GDir {
            return get(vector2.x, vector2.y)
        }
    }
}