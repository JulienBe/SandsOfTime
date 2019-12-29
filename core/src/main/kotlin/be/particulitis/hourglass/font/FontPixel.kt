package be.particulitis.hourglass.font

import be.particulitis.hourglass.common.GGraphics
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.comp.CompSpace
import com.badlogic.gdx.Gdx

class FontPixel private constructor(var desiredX: Float, var desiredY: Float) {

    var x = (charWidth * fontWidth) / 4f
    var y = (charHeight * fontWidth) / 4f
    var palette = Colors.scoreFont

    fun act(delta: Float) {
        x -= (x - desiredX) * delta * 4
        y -= (y - desiredY) * delta * 4
    }

    fun draw(space: CompSpace) {
        draw(space.x, space.y)
    }

    fun draw(offsetX: Float, offsetY: Float) {
        GGraphics.batch.draw(palette.scale[1], x + offsetX, y + offsetY, fontWidth)
    }

    companion object {

        val fontWidth = 2f
        val charWidth = 4 * fontWidth
        val charHeight = 5 * fontWidth

        private val instantiate: Map<Char, List<Offsets>> = Gdx.files.internal("fonts")
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

        fun get(index: Int, c: Char, offsetX: Float, offsetY: Float): FontChar {
            val char = get(index, c)?.let { FontChar(it) }!!
            char.pixels.forEach { p ->
                p.desiredX += offsetX
                p.desiredY += offsetY
                if (GRand.bool()) {
                    p.x = p.desiredX + GRand.gauss(70f)
                    p.y = p.desiredY + GRand.gauss(1f)
                } else {
                    p.x = p.desiredX + GRand.gauss(1f)
                    p.y = p.desiredY + GRand.gauss(70f)
                }
            }
            return char
        }

        fun get(index: Int, c: Char, width: Int = 2): List<FontPixel>? {
            return instantiate[c]?.mapIndexed { i, offset ->
                val pixel = mutableListOf<FontPixel>()
                // add extra pixels
                for (x in 0 until width) {
                    for (y in 0 until width) {
                        pixel.add(FontPixel(
                                (index * charWidth * width) + (((offset.xF * width) + x) * fontWidth),
                                ((offset.yF * width) + y) * fontWidth
                                ))
                    }
                }
                pixel
            }!!.flatten()
        }
    }

}

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