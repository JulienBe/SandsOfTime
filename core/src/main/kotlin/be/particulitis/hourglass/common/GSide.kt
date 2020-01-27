package be.particulitis.hourglass.common

enum class GSide(val pushX: Float, val pushY: Float, val mulX: Float, val mulY :Float, val angle: Float) {
    LEFT  (-1f, +0f, -1f, +1f, 180f),
    RIGHT (+1f, +0f, -1f, +1f, 0f),
    TOP   (+0f, +1f, +1f, -1f, 90f),
    BOTTOM(+0f, -1f, +1f, -1f, 270f),
    NONE  (-5f, 5f, 0f, 0f, 333f)

}