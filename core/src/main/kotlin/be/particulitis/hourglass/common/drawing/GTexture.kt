package be.particulitis.hourglass.common.drawing

object GTexture {
    const val w = 4
    const val h = 4
    val wall = arrayListOf(
        GPalette.D_GRAY, GPalette.L_GRAY, GPalette.L_GRAY, GPalette.WHITEISH,
        GPalette.D_GRAY, GPalette.D_GRAY, GPalette.D_GRAY, GPalette.L_GRAY,
        GPalette.D_GRAY, GPalette.D_GRAY, GPalette.D_GRAY, GPalette.L_GRAY,
        GPalette.D_BLUE, GPalette.D_GRAY, GPalette.D_GRAY, GPalette.D_GRAY
    )
    val xIndexes = Array(w * h) { (it % w).toFloat() }
    val yIndexes = Array(w * h) { (it / h).toFloat() }
    fun x(index: Int): Float {
        return xIndexes[index]
    }
    fun y(index: Int): Float {
        return yIndexes[index]
    }
}