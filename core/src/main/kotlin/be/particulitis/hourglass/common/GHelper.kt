package be.particulitis.hourglass.common

import be.particulitis.hourglass.gamedata.graphics.Anims33
import com.badlogic.gdx.Gdx

object GHelper {
    val percentX get() = ((Gdx.input.x.toFloat() / Gdx.graphics.width) -
            // offset by screen unused
            (1 - GResolution.percentageUsedX) / 2f) /
            // scale to screen used
            GResolution.percentageUsedX
    val percentY get(): Float = 1 - (
                // screen percentage
            (((Gdx.input.y).toFloat() / Gdx.graphics.height) -
                // offset by screen unused
            (1 - GResolution.percentageUsedY) / 2f) /
                // scale to screen used
            GResolution.percentageUsedY)
    val x get() = percentX * GResolution.areaDim
    val y get() = percentY * GResolution.areaDim

    fun mirrorAnimHorizontal(anim: Anims33): List<IntArray> {
        return anim.frames.map { ints ->
            intArrayOf(
                    ints[2], ints[1], ints[0],
                    ints[5], ints[4], ints[3],
                    ints[8], ints[7], ints[6]
            )
        }.toCollection(arrayListOf())
    }

    fun mirrorAnimVertical(anim: Anims33): List<IntArray> {
        return anim.frames.map { ints ->
            intArrayOf(
                    ints[6], ints[7], ints[8],
                    ints[0], ints[1], ints[2],
                    ints[3], ints[4], ints[5]
            )
        }.toCollection(arrayListOf())
    }


    fun rotate90(anim: Anims33): List<IntArray> {
        return anim.frames.map { ints ->
            intArrayOf(
                    ints[6], ints[3], ints[0],
                    ints[7], ints[4], ints[1],
                    ints[8], ints[5], ints[2]
            )
        }.toCollection(arrayListOf())
    }

    fun rotate45(anim: Anims33): List<IntArray> {
        return anim.frames.map { ints ->
            intArrayOf(
                    ints[3], ints[0], ints[1],
                    ints[6], ints[4], ints[2],
                    ints[7], ints[8], ints[5]
            )
        }.toCollection(arrayListOf())
    }

    fun rotate270(anim: Anims33): List<IntArray> {
        return anim.frames.map { ints ->
            intArrayOf(
                    ints[2], ints[5], ints[8],
                    ints[1], ints[4], ints[7],
                    ints[0], ints[3], ints[6]
            )
        }.toCollection(arrayListOf())
    }

}