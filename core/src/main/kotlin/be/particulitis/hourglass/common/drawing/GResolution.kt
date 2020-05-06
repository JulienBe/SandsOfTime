package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.Gdx
import kotlin.math.max

object GResolution {
    const val areaH = 200f
    const val areaW = 320f
    const val areaHH = areaH / 2f
    const val areaHW = areaW / 2f
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
        screenWidth = max(areaW * ratio, areaW)
        screenHeight = max(areaH / ratio, areaH)
        baseX = max((screenWidth - screenHeight) / 2f, 0f)
        baseY = max((screenHeight - screenWidth) / 2f, 0f)
    }
}