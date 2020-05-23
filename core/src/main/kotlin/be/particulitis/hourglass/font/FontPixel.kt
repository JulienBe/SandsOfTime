package be.particulitis.hourglass.font

import be.particulitis.hourglass.common.GHistoryFloat
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.drawing.GPalette
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import ktx.collections.GdxArray
import kotlin.math.abs

class FontPixel internal constructor(var desiredX: Float, var desiredY: Float, var primary: Boolean, var initDelay: Float) {

    val x = GHistoryFloat(trailSize)
    val y = GHistoryFloat(trailSize)
    private var speed = 1f
    private var snapped = false
    var boost = false
    var tr = if (primary) mainColor else secondaryColor
    var myMainColor = mainColor
    var mySecondaryColor = secondaryColor

    init {
        if (GRand.bool()) {
            x.fill(desiredX + GRand.gauss(25f))
            y.fill(desiredY + GRand.gauss(25f))
        } else {
            x.fill(desiredX + GRand.gauss(5f))
            y.fill(desiredY + GRand.gauss(5f))
        }
    }

    fun act(delta: Float): Boolean {
        initDelay -= delta
        if (initDelay > 0)
            return false
        mvtDiag(delta)
        if (!snapped && abs(x.get() - desiredX) + abs(y.get() - desiredY) < 0.1f) {
            snapped = true
            boost = true
            x.fill(desiredX)
            y.fill(desiredY)
        }
        return true
    }

    private fun mvtDiag(delta: Float) {
        if (abs(x.get() - desiredX) > abs(y.get() - desiredY))
            x.add(x.get() - ((x.get() - desiredX) * delta * 3 * speed))
        else
            y.add(y.get() - ((y.get() - desiredY) * delta * 3 * speed))
    }

    fun copyFrom(fontPixel: FontPixel) {
        desiredY = fontPixel.desiredY
        desiredX = fontPixel.desiredX
        snapped = false
        boost = false
        tr = fontPixel.tr
        primary = fontPixel.primary
    }

    fun updateColor(mainColor: TextureRegion, secondaryColor: TextureRegion) {
        tr = if (primary)
            mainColor
        else
            secondaryColor
        myMainColor = mainColor
        mySecondaryColor = secondaryColor
    }

    companion object {

        const val trailSize = 3
        val mainColor = GPalette.DARK_GREY.tr
        val secondaryColor = GPalette.LIGHT_GREY.tr

        private val instantiate: Map<Char, List<Pair<Int, Offsets>>> = Gdx.files.internal("fonts")
                .readString()
                .replace("SPACE", " ")
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

        fun get(index: Int, c: Char, width: Int = 1, existingPool: GdxArray<FontPixel> = GdxArray()): List<FontPixel> {
            val list = instantiate[c] ?: error("Char '$c' isn't present")
            var delay = 0f
            return list.mapIndexed { i, pair ->
                val pixel = mutableListOf<FontPixel>()
                // keeping -1 around so it's easier to reason about where the pixel is in the car
                if (pair.first != -1) {
                    val _1 = isPresent(list, i - 3)
                    val _2 = if (i % 3 == 2) false else isPresent(list, i - 2)
                    val _5 = if (i % 3 == 2) false else isPresent(list, i + 1)
                    val p = initPixel(index, width, pair.second, existingPool, _1, _2, _5, delay)
                    delay += p.size * 0.0015f
                    pixel.addAll(p)
                }
                pixel
            }.filter { it.isNotEmpty() }.flatten()
        }

        // 0 1 2
        // 3   5
        // 6 7 8
        private val tmpPixelList = GdxArray<FontPixel>(9)
        private fun initPixel(index: Int, width: Int, offset: Offsets, existingPool: Array<FontPixel>, _1: Boolean, _2: Boolean, _5: Boolean, delay: Float): GdxArray<FontPixel> {
            var pixelDelay = 0f
            tmpPixelList.clear()
            val desiredX = (index * width * 3) + (offset.xF * width) + (index) // base + char place + space between chars
            val desiredY = offset.yF * width
            for (y in 0 until width) {
                for (x in 0 until width) {
                    val p: FontPixel = if (existingPool.isEmpty)
                        FontPixel(desiredX, desiredY, true, 0f)
                    else
                        existingPool.pop()
                    p.desiredX = desiredX + x
                    p.desiredY = desiredY - y
                    p.tr = mainColor
                    p.primary = true
                    p.initDelay = (index / 4f) + delay + pixelDelay
                    pixelDelay += 0.01f
                    tmpPixelList.add(p)
                }
            }
            // **.
            // **.
            // **.
            if (!_5)
                for (i in 0 until width * width)
                    if (i % width == (width - 1)) {
                        tmpPixelList[i].tr = secondaryColor
                        tmpPixelList[i].primary = false
                    }
            // ...
            // ***
            // ***
            if (!_1)
                for (i in 0 until width * width)
                    if (i < width) {
                        tmpPixelList[i].tr = secondaryColor
                        tmpPixelList[i].primary = false
                    }
            // **.
            // ***
            // ***
            if (!_2) {
                tmpPixelList[width - 1].tr = secondaryColor
                tmpPixelList[width - 1].primary = false
            }
            return tmpPixelList
        }

        private fun isPresent(list: List<Pair<Int, Offsets>>, i: Int): Boolean {
            return i >= 0 && i < list.size && list[i].first != -1
        }

        fun width(text: String, w: Int): Float {
            return text.length * w * 4f
        }

        fun height(w: Int): Float {
            return 5f * w
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