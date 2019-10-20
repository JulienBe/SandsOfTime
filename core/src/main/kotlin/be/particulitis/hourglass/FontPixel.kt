package be.particulitis.hourglass

import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.Gdx

class FontPixel private constructor(var desiredX: Float, var desiredY: Float) {

    fun act(delta: Float) {
        x -= (x - desiredX) * delta * 4
        y -= (y - desiredY) * delta * 4
    }

    fun draw(space: CompSpace) {
        FirstScreen.batch.draw(Colors.scoreFont, space.x + x, space.y + y, fontWidth)
    }

    var x = (charWidth * fontWidth) / 4f
        private set
    var y = (charHeight * fontWidth) / 4f
        private set

    companion object {

        val fontWidth = 2f
        val charWidth = 4 * fontWidth
        val charHeight = 5 * fontWidth

        val instantiate: Map<Char, List<Offsets>> = Gdx.files.internal("fonts")
                .readString()
                .split("---")
                .map { it.filter { char -> char.category != CharCategory.CONTROL} }
                // so far it looks like : $111110011111010, %101001010100101, *101010111010101, +000010111010000, ...
                .associateBy(
                        { it[0] },
                        { charDef -> charDef
                                    .subSequence(1, charDef.length)
                                    .mapIndexed { index, c -> if (c == '1') index else -1 }
                                    .filter { it != -1 }
                                    .map { Offsets.values()[it] }
                        }
                )

        fun get(index: Int, c: Char): List<FontPixel>? {
            return instantiate[c]?.map {
                FontPixel(index * charWidth + it.xF * fontWidth, it.yF * fontWidth)
            }
        }
    }

}

data class FontCharTemplate(val nbPixels: Int, val seq: CharSequence)

enum class Offsets() {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    ELVEN,
    TWELVE,
    THIRTEEN,
    FOURTEEN,
    FIFTEEN;

    val x = ordinal % 3
    val y = 4 - (ordinal / 3)
    val xF = x.toFloat()
    val yF = y.toFloat()
}