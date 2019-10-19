package be.particulitis.hourglass.gamedata

enum class Dim(val w: Float) {
    player(6f);

    val half = w / 2f
    val third = w / 3f

}