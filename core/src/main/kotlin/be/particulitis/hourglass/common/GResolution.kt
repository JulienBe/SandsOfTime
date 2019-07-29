package be.particulitis.hourglass.common

import com.badlogic.gdx.Gdx

object GResolution {
    const val areaDim = 300f
    const val areaHDim = areaDim / 2f
    var ratio = 0f
    var screenWidth = 0f
    var screenHeight = 0f
    var baseX = 0f
    var baseY = 0f

    init {
        compute()
    }

    fun compute() {
        ratio = Gdx.graphics.width / Gdx.graphics.height.toFloat()
        screenWidth = Math.max(areaDim * ratio, areaDim)
        screenHeight = Math.max(areaDim / ratio, areaDim)
        baseX = Math.max((screenWidth - screenHeight) / 2f, 0f)
        baseY = Math.max((screenHeight - screenWidth) / 2f, 0f)
    }
}