package be.particulitis.hourglass.gamedata

enum class Anims(val frames: ArrayList<IntArray>) {


    squareNoDir(arrayListOf(
            intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    +0, +0, +0
            ),
            intArrayOf(
                    +0, +0, +0,
                    +0, +0, +0,
                    +0, +0, +0
            ),
            intArrayOf(
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
    ));

    val size = frames.size
}