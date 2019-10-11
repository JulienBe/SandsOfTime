package be.particulitis.hourglass.common

import com.badlogic.gdx.Gdx
import kotlin.math.max
import kotlin.math.min

object GResolution {
    const val areaDim = 200f
    const val areaHDim = areaDim / 2f
    var ratio = 0f
    var screenWidth = 0f
    var screenHeight = 0f
    var baseX = 0f
    var baseY = 0f
    var percentageUsedX = 0f
    var percentageUsedY = 0f

    init {
        compute()
    }

    fun compute() {
        ratio = Gdx.graphics.width / Gdx.graphics.height.toFloat()
        screenWidth = max(areaDim * ratio, areaDim)
        screenHeight = max(areaDim / ratio, areaDim)
        baseX = max((screenWidth - screenHeight) / 2f, 0f)
        baseY = max((screenHeight - screenWidth) / 2f, 0f)
        percentageUsedX = min(1 / ratio, 1f)
        percentageUsedY = min(ratio, 1f)
    }
}