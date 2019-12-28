package be.particulitis.hourglass.font

import be.particulitis.hourglass.common.*
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector2

/**
 * Handle its own time
 */
class FontPrettyDisplay(text: String, var x: Float, var y: Float) {

    private var pixels = text.mapIndexed { index, c -> FontPixel.get(index, c, x, y) }.flatMap { it.pixels }
    private var currentIndex = 0
    private var time = .1f
    private var anims = arrayListOf<FontAnim>()

    private fun getListFrom(fromX: Float, fromY: Float) =
            pixels.sortedBy { Vector2.dst(fromX, fromY, it.x, it.y) }

    fun draw(batch: Batch) {
        if (Gdx.input.justTouched())
            anims.add(FontAnim(getListFrom(GResolution.baseX + GHelper.x, GResolution.baseY + GHelper.y).toMutableList()))
        time += Gdx.graphics.deltaTime
        currentIndex = (time * 30).toInt()
        pixels.filterIndexed { index, pixel ->
            index < currentIndex
        }.forEach { pixel ->
            pixel.act(Gdx.graphics.deltaTime)
            pixel.draw(0f, 0f)
        }
        anims.removeIf { it.act() }
    }
}