package be.particulitis.hourglass.font

import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.gamedata.graphics.Colors
import com.badlogic.gdx.Gdx
import ktx.collections.GdxArray
import kotlin.math.*

class FontPixel private constructor(var desiredX: Float, var desiredY: Float) {

    var x = desiredX + GRand.gauss(5)
    var y = desiredY + GRand.gauss(5)
    var oldX = x
    var oldY = y
    var palette = Colors.scoreFont
    var couldBeRemoved = false
    var scale = 1
    var snapped = false
    var boost = false
    var dirX = 0f
    var dirY = 0f
    var speed = 1f

    fun act(delta: Float) {
        if ((GTime.time * 3f).roundToInt() % 2 == 0) {
            oldX = x
            dirX += (x - desiredX) / 2f
            dirX *= 0.9f
            x -= dirX * delta * speed
            x -= (x - desiredX) * delta * 2 * speed
        } else {
            oldY = y
            dirY += (y - desiredY) / 2f
            dirY *= 0.9f
            y -= dirY * delta * speed
            y -= (y - desiredY) * delta * 2 * speed
        }
        if (!snapped && abs(x - desiredX) + abs(y - desiredY) < 0.1f) {
            snapped = true
            boost = true
        }
    }

    fun draw(offsetX: Float, offsetY: Float, scale: Int, width: Float = fontWidth) {
        if (boost) {
            GSounds.pixelSnap.play(0.5f, 1 + ((x / GResolution.screenWidth) / 2f), 1f)
            GGraphics.batch.draw(GPalette.rand().scale[0], (x + offsetX).roundToInt().toFloat(), (y + offsetY).roundToInt().toFloat(), width)
        } else {
            GGraphics.batch.draw(palette.scale[scale], (x + offsetX).roundToInt().toFloat(), (y + offsetY).roundToInt().toFloat(), width)
        }
    }

    fun move(futureX: Float, futureY: Float) {
        desiredX = futureX
        desiredY = futureY
        snapped = false
        boost = false
    }

    companion object {

        const val fontWidth = 2f
        const val charWidth = 4 * fontWidth
        const val charHeight = 5 * fontWidth

        private val instantiate: Map<Char, List<Pair<Int, Offsets>>> = Gdx.files.internal("fonts")
                .readString()
                .split("---")
                .map { it.filter { char -> char.category != CharCategory.CONTROL } }
                // so far it looks like : $111110011111010, %101001010100101, *101010111010101, +000010111010000, ...
                .associateBy(
                        { it[0] },
                        { charDef -> charDef
                                    .subSequence(1, charDef.length)
                                    .mapIndexed { index, c -> if (c == '1') index else -1 }
                                    //.filter { it != -1 }
                                    .map { Pair(it, if (it != -1) Offsets.values()[it] else Offsets.ZERO) }
                        }
                )

        fun get(index: Int, c: Char, offsetX: Float, offsetY: Float): FontChar {
            val char = FontChar(get(index, c))
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

        fun get(index: Int, c: Char, width: Int = 1, existingPool: GdxArray<FontPixel> = GdxArray()): List<FontPixel> {
            val list = instantiate[c] ?: error("Char $c isn't present")
            return list.mapIndexed { i, pair ->
                val pixel = mutableListOf<FontPixel>()
                // keeping -1 around so it's easier to reason about where the pixel is in the car
                if (pair.first != -1) {
                    val hasTop = isPresent(list, i - 3)
                    val hasBottom = isPresent(list, i + 3)
                    val hasLeft = isPresent(list, i - 1)
                    val hasRight = isPresent(list, i + 1)
                    val hasBottomLeft = isPresent(list, i + 2)
                    val hasBottomRight = isPresent(list, i + 4)
                    val hasTopLeft = isPresent(list, i - 4)
                    val hasTopRight = isPresent(list, i - 2)
                    val offset = pair.second
                    for (x in 0 until width) {
                        for (y in 0 until width) {
                            val p = initFontPixel(index, width, offset, x, y, existingPool)
                            pixel.add(p)
                            p.snapped = false
                            p.couldBeRemoved = false
                            when ((x * width) + y) {
                                // bottom left
                                0 -> if (!hasBottom && !hasLeft && !hasBottomLeft && !hasTopLeft && hasTop)
                                    p.couldBeRemoved = true
                                // top left
                                width - 1 -> if (!hasTop && !hasLeft && !hasTopLeft && !hasBottomLeft)
                                    p.couldBeRemoved = true
                                // bottom right
                                width -> if (!hasBottom && !hasRight && !hasBottomRight && !hasBottomLeft && hasLeft && hasTop)
                                    p.couldBeRemoved = true
                                // top right
                                (width * width) - 1 -> if (!hasRight && hasLeft && !hasTop && !hasTopRight)
                                    p.couldBeRemoved = true
                            }
                        }
                    }
                }
                pixel
            }.filter { it.isNotEmpty() }.flatten()
        }

        private fun isPresent(list: List<Pair<Int, Offsets>>, i: Int): Boolean {
            return i >= 0 && i < list.size && list[i].first != -1
        }

        private fun initFontPixel(index: Int, width: Int, offset: Offsets, x: Int, y: Int, existingPool: GdxArray<FontPixel>): FontPixel {
            val desiredX = (index * charWidth * width) + (((offset.xF * width) + x) * fontWidth)
            val desiredY = ((offset.yF * width) + y) * fontWidth
            val p: FontPixel = if (existingPool.isEmpty)
                FontPixel(desiredX, desiredY)
            else
                existingPool.pop()
            p.desiredX = desiredX
            p.desiredY = desiredY
            return p
        }

        fun width(text: String, w: Int): Float {
            return text.length * w * charWidth
        }

        fun height(w: Int): Float {
            return charHeight * w
        }
    }
}

enum class Offsets {
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