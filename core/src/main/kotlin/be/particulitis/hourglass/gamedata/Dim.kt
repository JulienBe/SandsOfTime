package be.particulitis.hourglass.gamedata

enum class Dim(val w: Float) {
    Player(6f),
    Enemy(12f);

    val half = w / 2f
    val third = w / 3f

}