package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.common.GHelper

enum class Anims(val frames: List<IntArray>) {

    SquareNoDir(arrayListOf(intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    +0, -1, +0,
                    +0, +0, +0
            ), intArrayOf(
                    -1, -1, -1,
                    -1, -2, -1,
                    -1, -1, -1
            ), intArrayOf(
                    -2, -2, -2,
                    -2, -1, -2,
                    -2, -2, -2
            ), intArrayOf(
                    -1, -1, -1,
                    -1, +0, -1,
                    -1, -1, -1
            )
    )),
    SquareLeft(arrayListOf(
            intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    -2, +0, +0,
                    +0, +0, +0
            ), intArrayOf(
                    -2, +0, +0,
                    -1, -2, +0,
                    -2, +0, +0
            ), intArrayOf(
                    -1, -2, +0,
                    +0, -1, -2,
                    -1, -2, +0
            ), intArrayOf(
                    +0, -1, -2,
                    +0, +0, -1,
                    +0, -1, -2
            ), intArrayOf(
                    +0, +0, -1,
                    +0, +0, +0,
                    +0, +0, -1
            )
            )),
    SquareRight(GHelper.mirrorAnimHorizontal(SquareLeft)),
    SquareUp(GHelper.rotate270(SquareRight)),
    SquareDown(GHelper.mirrorAnimVertical(SquareUp)),
    SquareUpRight(arrayListOf(
            intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    -2, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    -2, -2, +0,
                    -1, -2, +0
            ), intArrayOf(
                    -1, -2, -2,
                    +0, -1, -2,
                    +0, +0, -1
            ), intArrayOf(
                    +0, -1, -2,
                    +0, +0, -1,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, -1,
                    +0, +0, +0,
                    +0, +0, +0
            )
            )),
    SquareDownRight(GHelper.rotate90(SquareUpRight)),
    SquareDownLeft(GHelper.rotate90(SquareDownRight)),
    SquareUpLeft(GHelper.rotate90(SquareDownLeft))
    ;

    val size = frames.size

}