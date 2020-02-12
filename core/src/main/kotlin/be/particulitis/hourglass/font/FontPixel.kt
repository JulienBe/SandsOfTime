package be.particulitis.hourglass.font

import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.gamedata.graphics.Colors
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.collections.GdxArray
import kotlin.math.*

class FontPixel private constructor(var desiredX: Float, var desiredY: Float, var tr: TextureRegion) {

    var x = desiredX + GRand.gauss(5)
    var y = desiredY + GRand.gauss(5)
    var oldX = x
    var oldY = y
    var shade = Colors.scoreFont
    var couldBeRemoved = false
    var scale = 1
    var snapped = false
    var boost = false
    var dirX = 0f
    var dirY = 0f
    var speed = 1f
    var lightId = -1

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
            GSounds.pixelSnap.play(0.1f, 1 + ((x / GResolution.areaW) / 2f), 1f)
        }
    }

    fun move(futureX: Float, futureY: Float) {
        desiredX = futureX
        desiredY = futureY
        snapped = false
        boost = false
    }

    companion object {

        const val fontWidth = 3f
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
                            // left side
                    val _0 = if (i % 3 == 0) false else isPresent(list, i - 4)
                    val _1 = isPresent(list, i - 3)
                            // right side
                    val _2 = if (i % 3 == 2) false else isPresent(list, i - 2)

                    val _3 = if (i % 3 == 0) false else isPresent(list, i - 1)
                    val _5 = if (i % 3 == 2) false else isPresent(list, i + 1)

                    val _6 = if (i % 3 == 0) false else isPresent(list, i + 2)
                    val _7 = isPresent(list, i + 3)
                    val _8 = isPresent(list, i + 4)

                    val offset = pair.second
                    for (x in 0 until width) {
                        for (y in 0 until width) {
                            val p = initFontPixel(index, width, offset, x, y, existingPool, getTr(
                                    _0, _1, _2,
                                    _3,     _5,
                                    _6, _7, _8))
                            pixel.add(p)
                            p.snapped = false
                            p.couldBeRemoved = false
                            if (_7 && _3 && _5 && _1 && GRand.nextInt(100) == 0)
                                p.couldBeRemoved
                            when ((x * width) + y) {
                                // bottom left
                                0 -> if (!_7 && !_3 && !_6 && !_0 && _1)
                                    p.couldBeRemoved = true
                                // top left
                                width - 1 -> if (!_1 && !_3 && !_0 && !_6)
                                    p.couldBeRemoved = true
                                // bottom right
                                width -> if (!_7 && !_5 && !_8 && !_6 && _3 && _1)
                                    p.couldBeRemoved = true
                                // top right
                                (width * width) - 1 -> if (!_5 && _3 && !_1 && !_2)
                                    p.couldBeRemoved = true
                            }
                            // 2 2 2
                            // 4   4
                            // 3 6 3
                            // 2   2
                            /**
                             *  000
                                111
                                101
                                111
                                101
                             */

                        }
                    }
                }
                pixel
            }.filter { it.isNotEmpty() }.flatten()
        }

        // TODO find something pretty with these fonts
        private fun getTr(_0: Boolean, _1: Boolean, _2: Boolean, _3: Boolean, _5: Boolean, _6: Boolean, _7: Boolean, _8: Boolean): TextureRegion {
//            println(" ")
//            println(" ")
//            println("\nGetting ================ ===============  ")
//            println("Getting ================ ===============  ")
//            println("Getting \n$_0\t $_1\t $_2 \n$_3 \t\t $_5\n$_6\t $_7\t $_8")
//            println(" ")
            var name = ""
//            name +=    if (_0) "0" else ""
            name +=    if (_1) "1" else ""
//            name +=    if (_2) "2" else ""
            name +=    if (_3) "3" else ""
            name +=            "4"
            name +=    if (_5) "5" else ""
//            name +=    if (_6) "6" else ""
            name +=    if (_7) "7" else ""
//            name +=    if (_8) "8" else ""
            return if (name.length > 4)
                GGraphics.tr("font_pixel_4_not_frame")
            else
                GGraphics.tr("font_pixel_${name}_not_frame")
        }

        private fun isPresent(list: List<Pair<Int, Offsets>>, i: Int): Boolean {
            return i >= 0 && i < list.size && list[i].first != -1
        }

        private fun initFontPixel(index: Int, width: Int, offset: Offsets, x: Int, y: Int, existingPool: GdxArray<FontPixel>, tr: TextureRegion): FontPixel {
            val desiredX = (index * charWidth * width) + (((offset.xF * width) + x) * fontWidth)
            val desiredY = ((offset.yF * width) + y) * fontWidth
            val p: FontPixel = if (existingPool.isEmpty)
                FontPixel(desiredX, desiredY, tr)
            else
                existingPool.pop()
            p.desiredX = desiredX
            p.desiredY = desiredY
            p.tr = tr
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