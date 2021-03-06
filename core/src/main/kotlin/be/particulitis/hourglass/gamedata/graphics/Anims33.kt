package be.particulitis.hourglass.gamedata.graphics

import be.particulitis.hourglass.common.GHelper

enum class Anims33(val frames: List<IntArray>) {

    ShootFromRight(arrayListOf(intArrayOf(
                    +0, +0, +0,
                    +0, +0, -2,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    +0, -2, -1,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    -2, -1, +0,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    -1, +0, +0,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    +0, +0, +0
            ), intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    +0, +0, +0
            )
    )),
    ShootFromDownRight(GHelper.rotate45(ShootFromRight)),
    ShootFromDown(GHelper.rotate45(ShootFromDownRight)),
    ShootFromDownLeft(GHelper.rotate45(ShootFromDown)),
    ShootFromLeft(GHelper.rotate45(ShootFromDownLeft)),
    ShootFromUpLeft(GHelper.rotate45(ShootFromLeft)),
    ShootFromUp(GHelper.rotate45(ShootFromUpLeft)),
    ShootFromUpRight(GHelper.rotate45(ShootFromUp)),
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