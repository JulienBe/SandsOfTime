package be.particulitis.hourglass.font

import be.particulitis.hourglass.common.GPalette
import com.badlogic.gdx.Gdx
import kotlin.math.roundToInt

class FontAnim(var wave: MutableList<FontPixel>) {

    private var nextStep = speed
    private var time = 0f
    private var palette = GPalette.values().random()

    fun act(): Boolean {
        val toProcess = wave.subList(0, if (wave.size > 4) (wave.size / 10f).roundToInt() else 1)
        toProcess.forEach { it.palette = palette }
        time += Gdx.graphics.deltaTime
        if (nextStep < time) {
            nextStep = time + speed
            wave.removeAll(toProcess)
        }
        return wave.isEmpty()
    }

    companion object {
        const val speed = .05f
    }
}