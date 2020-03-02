package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import com.artemis.*
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import kotlin.math.min

class SysHourglassDisplay : BaseSystem() {

    private var nextDrop = 0f
    private var colOffsets = intArrayOf(
            0, 0, 1, 1, 2, 2, 3, 3, 2, 2, 1, 1, 0, 0
    )
    var hourglass = arrayListOf(
            intArrayOf(0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0),
            intArrayOf(  0,0,0,0,0  ),
            intArrayOf(  0,0,0,0,0  ),
            intArrayOf(    0,0,0    ),
            intArrayOf(    0,0,0    ),
            intArrayOf(      0      ),
            intArrayOf(      0      ),
            intArrayOf(    0,0,0    ),
            intArrayOf(    0,0,0    ),
            intArrayOf(  0,0,0,0,0  ),
            intArrayOf(  0,0,0,0,0  ),
            intArrayOf(0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0)
    )
    private var enemy = false
    private val height: Float
    private var transition = 2f

    init {
        for (i in 0..hourglass.lastIndex/2)
            hourglass[i].indices.forEach {
                hourglass[i][it] = yellow
            }
        height = hourglass.lastIndex.toFloat()
    }

    override fun processSystem() {
        transition += GTime.delta
        val direction = if (enemy) -1 else 1
        if (nextDrop < GTime.time) {
            nextDrop = GTime.time + (0.0055f * GTime.phaseTime)
            for (rowIndex in 0..hourglass.lastIndex) {
                val fallen = fall(rowIndex, direction)
                if (fallen && GRand.bool())
                    break
            }
        }

        GGraphics.batch.shader = null
        val baseY = if (enemy) 0f else height * 2f
        val yMul = if (enemy) 1f else -1f
        val otherY = if (enemy) height * 2f else 0f
        val otherYMul = if (enemy) -1f else 1f
        if (!GGraphics.batch.isDrawing)
            GGraphics.batch.begin()
        hourglass.forEachIndexed { rowIndex, ints ->
            ints.forEachIndexed { colIndex, i ->
                GGraphics.batch.draw(imgs[i], x(rowIndex, colIndex), y(baseY, rowIndex, yMul, otherY, otherYMul), 2f, 2f)
            }
        }
        GGraphics.batch.end()
        if (GTime.justSwitched)
            transition()
    }

    private fun fall(rowIndex: Int, direction: Int): Boolean {
        var fallen = false
        for (columnIndex in (0..hourglass[rowIndex].lastIndex).reversed())
            if (hourglass[rowIndex][columnIndex] == yellow)
                if (swapIfAvailable(rowIndex, columnIndex, rowIndex + direction, columnIndex, yellow))
                    fallen = true
        if (!fallen)
            for (columnIndex in (0..hourglass[rowIndex].lastIndex).reversed())
                if (hourglass[rowIndex][columnIndex] == yellow)
                    if (swapIfAvailable(rowIndex, columnIndex, rowIndex + direction, columnIndex + if (GRand.nextBoolean()) 1 else -1, yellow))
                        fallen = true
        return fallen
    }

    private fun transition() {
        transition = 0f
        enemy = !enemy
        nextDrop = GTime.time + 0.5f
        val direction = if (enemy) 1 else -1
        for (i in 0..30)
            for (rowIndex in 0..hourglass.lastIndex)
                fall(rowIndex, direction)
    }

    private val interpolationY = Interpolation.circle
    private fun y(baseY: Float, rowIndex: Int, yMul: Float, otherY: Float, otherYMul: Float): Float {
        val desiredY = baseY + (rowIndex * 2f * yMul)
        val oppositeY = otherY + (rowIndex * 2f * otherYMul)
        return interpolationY.apply(oppositeY, desiredY, min(transition * 2f, 1f))
    }
    private fun x(rowIndex: Int, colIndex: Int): Float {
        return (colOffsets[rowIndex] + colIndex) * 2f
    }

    private fun swapIfAvailable(rowFrom: Int, colFrom: Int, rowTo: Int, colTo: Int, me: Int): Boolean {
        val colDiff = colOffsets[rowFrom] - colOffsets[MathUtils.clamp(rowTo, 0, colOffsets.lastIndex)]
        return if (existAndNotMe(rowTo, colTo + colDiff, me)) {
            hourglass[rowTo][colTo + colDiff] = me
            hourglass[rowFrom][colFrom] = background
            true
        } else {
            false
        }
    }

    private fun existAndNotMe(row: Int, col: Int, me: Int): Boolean {
        return row >= 0 && row <= hourglass.lastIndex && col >= 0 && col <= hourglass[row].lastIndex && hourglass[row][col] != me
    }

    companion object {
        const val empty = -1
        const val background = 0
        const val yellow = 1
        val imgs = arrayListOf(
                GGraphics.img("squares/square_purple").front,
                GGraphics.img("squares/square_yellow").front
        )
    }

}

class Grain(var x: Int, var y: Int)