package be.particulitis.hourglass.common

enum class GSide(val normal: Float, val x: Float, val y: Float) {
    LEFT(180f, -1f, 0f), RIGHT(0f, 1f, 0f), TOP(90f, 0f, 1f), BOTTOM(180f, 0f, -1f), NONE(270f, -5f, 5f)
}