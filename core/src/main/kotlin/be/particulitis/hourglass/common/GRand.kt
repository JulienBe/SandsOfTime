package be.particulitis.hourglass.common

import be.particulitis.hourglass.common.drawing.GResolution
import java.util.*

object GRand: Random() {
    fun within(low: Float, high: Float): Float {
        return low + nextFloat() * high
    }

    fun withinButNot(low: Float, high: Float, lowNot: Float, highNot: Float): Float {
        assert((low < lowNot) || (high > highNot))
        for (i in 1..60) {
            val candidate = within(low, high)
            if (candidate < lowNot || candidate > highNot)
                return candidate
        }
        return within(low, high)
    }

    fun gauss(f: Float): Float {
        return (nextGaussian() * f).toFloat()
    }
    fun gauss(i: Int): Int {
        return (nextGaussian() * i).toInt()
    }

    fun floatExcludingPlease(min: Float, max: Float, exclusionStart: Float, exclusionStop: Float): Float {
        var f = float(min, max)
        var cpt = 0
        while (f in exclusionStart..exclusionStop || cpt++ > 150) {
            f = float(min, max)
        }
        return f
    }

    fun float(min: Float, max: Float): Float {
        return min + (nextFloat() * (max - min))
    }

    fun int(min: Int, max: Int): Int {
        return min + nextInt(max - min)
    }

    fun absGauss(fl: Float): Float {
        return Math.abs(gauss(fl))
    }

    fun absGauss(i: Int): Int {
        return Math.abs(gauss(i))
    }

    fun bool() = nextBoolean()
    fun bool(i: Int): Boolean {
        return next(i) == 0
    }

    fun randX(): Float {
        return float(0f, GResolution.areaW)
    }
    fun randY(): Float {
        return float(0f, GResolution.areaH)
    }

    fun oneOrMinus(): Int {
        return if (nextBoolean()) 1 else -1
    }

    fun oneOrMinusF(): Float {
        return if (nextBoolean()) 1f else -1f
    }
}