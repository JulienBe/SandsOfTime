package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.GHistoryFloat
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GShader
import be.particulitis.hourglass.gamedata.setups.SParticles
import com.artemis.*
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import ktx.collections.GdxMap
import ktx.collections.gdxMapOf
import java.util.ArrayList
import kotlin.math.max

class SysHourglassDisplay : BaseSystem() {

    private val grid = arrayListOf(
            intArrayOf(W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W),
            intArrayOf(W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W),
            intArrayOf(W, W,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W, W),
            intArrayOf(W, W,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W, W),
            intArrayOf(0, W,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W,0),
            intArrayOf(0, W, W,0,0,0,0,0,0,0,0,0,0,0,0, W, W,0),
            intArrayOf(0, W, W,0,0,0,0,0,0,0,0,0,0,0,0, W, W,0),
            intArrayOf(0,0, W, W,0,0,0,0,0,0,0,0,0,0, W, W,0,0),
            intArrayOf(0,0,0, W, W,0,0,0,0,0,0,0,0, W, W,0,0,0),
            intArrayOf(0,0,0,0, W, W,0,0,0,0,0,0, W, W,0,0,0,0),
            intArrayOf(0,0,0,0,0, W, W,0,0,0,0, W, W,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0, W,0,0,0,0, W,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0, W, W,0,0, W, W,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0, W, W,0,0, W, W,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0, W,0,0,0,0, W,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0, W, W,0,0,0,0, W, W,0,0,0,0,0),
            intArrayOf(0,0,0,0, W, W,0,0,0,0,0,0, W, W,0,0,0,0),
            intArrayOf(0,0,0, W, W,0,0,0,0,0,0,0,0, W, W,0,0,0),
            intArrayOf(0,0, W, W,0,0,0,0,0,0,0,0,0,0, W, W,0,0),
            intArrayOf(0, W, W,0,0,0,0,0,0,0,0,0,0,0,0, W, W,0),
            intArrayOf(0, W, W,0,0,0,0,0,0,0,0,0,0,0,0, W, W,0),
            intArrayOf(0, W,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W,0),
            intArrayOf(W, W,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W, W),
            intArrayOf(W, W,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W, W),
            intArrayOf(W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0, W),
            intArrayOf(W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W)
    )
    private val sand = gdxMapOf<Int, Grain>()
    private val height = grid.size.toFloat()
    private var nextDrop = 0f
    private var transitionTime = 0f
    private var previousPhase = GTime.enemyPhase
    private var interpolation = Interpolation.fade
    private var alphaTransition = GHistoryFloat(shades.size * 2)
    private val x = 295f
    private val y = 200f - height
    private val shader = GShader.createShader("shaders/hourglass/vertex.glsl", "shaders/hourglass/fragment.glsl")
    private val fbo = DrawerTools.frameBuffer()
    private val spawnVector = Vector2()

    init {
        for (i in 0..120) {
            val grain = Grain()
            grain.updateGrid(grid)
            sand.put(grain.hashCode(), grain)
        }
        center.set((grid.first().size / 2).toFloat() - 0.5f, (height / 2f) - 0.5f)
    }

    override fun processSystem() {
        if (batch.isDrawing)
            batch.end()
        batch.shader = null
        batch.begin()
        transitionTime -= GTime.delta
        if (previousPhase != GTime.enemyPhase)
            transitionTime = 0.75f
        alphaTransition.add(computeAlphaTransition())
        for (i in 0 until shades.size)
            batch.draw(shades[i], x, y, alphaTransition.get(-(i * 2)))
        val gravity = if (GTime.enemyPhase) -1 else 1
        if (nextDrop < GTime.time) {
            nextDrop = GTime.time + dropFreq
            drop(gravity)
        }
//        val player = world.getSystem(TagManager::class.java).getEntity(Data.playerTag).getComponent(CompSpace::class.java)
        if (transitionTime > 0f) {
            if (GTime.enemyPhase) {
                val dst = interpolation.apply(0.75f, 0f, transitionTime) * 440f
                val candidates = sand.values().shuffled()
                spawnVector.set(dst, 0f)
//                for (i in 0..60) {
//                    spawnVector.setAngle(160f + (i * 2.2f))
//                    val origin = candidates[i]
//                    for (i in 0..3)
//                        SParticles.trailTarget(world, (x + origin.x) + spawnVector.x + GRand.gauss(2f), (y + origin.y) + spawnVector.y + GRand.gauss(2f), SParticles.blueAnim, player.centerX, player.centerY, 1f + GRand.absGauss(1f))
//                }
            }
        }
        if (transitionTime > 0.1f)
            pushToTheSides(gravity)
        else if (transitionTime > -0.2f)
            pushBack()
        sand.values().forEach {
            it.draw(gravity, grid, sand, world, alphaTransition.get(), x, y)
        }
        previousPhase = GTime.enemyPhase
        batch.end()
        batch.shader = null
    }

    private fun pushBack() {
        sand.values().forEach {
            if (GRand.nextBoolean() && GRand.nextBoolean())
                it.ifFreeMoveThere(-1, 0, grid)
        }
    }

    private fun pushToTheSides(gravity: Int) {
        sand.values().forEach {
            if (!it.ifFreeMoveThere(+1, -gravity, grid)) {
                it.ifFreeMoveThere(0, -gravity, grid)
            }
        }
    }

    private fun computeAlphaTransition(): Float {
        var transitionValue = MathUtils.clamp(interpolation.apply(0f, 1.08f, max(0f, transitionTime)), 0f, 1f)
        if (GTime.enemyPhase)
            transitionValue = 1f - transitionValue
        return transitionValue * 180f
    }

    private fun drop(gravity: Int) {
        sand.values().filterNot {
            if (it.ifFreeMoveThere(0, +gravity, grid)) {
                true
            } else {
                // just one for the 'viscosity'
                it.ifFreeMoveThere(if (GRand.bool()) 1 else -1, gravity, grid)
            }
        }.forEach {
            // they have not moved
            if (it.moved && it.bumper &&
                    // are basically surrounded
                    !it.isFree(+0, -gravity, grid) &&
                    !it.isFree(+0, +gravity, grid) &&
                    !it.isFree(+1, +gravity, grid) &&
                    !it.isFree(+1, -gravity, grid) &&
                    !it.isFree(-1, -gravity, grid) &&
                    !it.isFree(-1, +gravity, grid)) {
                it.bump()
                it.moved = false
            }
        }
    }

    companion object {
        const val W = 7
        const val dropFreq = 0.024f
        val batch = GGraphics.batch
        val hourglass = GGraphics.tr("hourglass")
        val shades = arrayListOf(
                GGraphics.tr("hourglass_shade_______"),
                GGraphics.tr("hourglass_shade______"),
                GGraphics.tr("hourglass_shade_____"),
                GGraphics.tr("hourglass_shade____"),
                GGraphics.tr("hourglass_shade___"),
                GGraphics.tr("hourglass_shade__"),
                GGraphics.tr("hourglass_shade_"),
                GGraphics.tr("hourglass")
        )
        var center = Vector2()
    }
}

class Grain(var x: Int = 9, var y: Int = 5) {

    private var justBumped = false
    var bumper = GRand.nextInt(6) == 5
    var moved = false
    private var normalColor = playerNormal
    private var trailAnim = playerTrail

    fun updateGrid(grid: ArrayList<IntArray>) {
        grid[y][x] = hashCode()
    }

    private fun moveTo(newX: Int, newY: Int, grid: ArrayList<IntArray>) {
        if (grid[y][x] == hashCode())
            grid[y][x] = 0

        x = newX
        y = newY
        grid[y][x] = hashCode()
        moved = true
    }

    fun bump() {
        justBumped = true
    }

    fun draw(gravity: Int, grid: ArrayList<IntArray>, sand: GdxMap<Int, Grain>, world: World, transitionValue: Float, x: Float, y: Float) {
        rotationVector.set(SysHourglassDisplay.center.x - this.x, SysHourglassDisplay.center.y - this.y)
        rotationVector.rotate(transitionValue)
        if (justBumped && !GTime.alternate) {
            matchColorWithPhase(GTime.enemyPhase)
            justBumped = false
            if (GRand.bool()) {
                sand[getGridValue(grid, this.x, this.y + gravity)]?.bump()
                sand[getGridValue(grid, this.x, this.y - gravity)]?.bump()
                if (GRand.bool()) {
                    sand[getGridValue(grid, this.x + 1, this.y + gravity)]?.bump()
                    sand[getGridValue(grid, this.x - 1, this.y + gravity)]?.bump()
                }
            }
            SParticles.trail(world, x + SysHourglassDisplay.center.x + rotationVector.x, y + SysHourglassDisplay.center.y + rotationVector.y, trailAnim, 1f)
        } else {
            SysHourglassDisplay.batch.draw(normalColor, x + SysHourglassDisplay.center.x + rotationVector.x, y + SysHourglassDisplay.center.y + rotationVector.y, 1f, 1f)
        }
    }

    fun isFree(offsetX: Int, offsetY: Int, grid: ArrayList<IntArray>): Boolean {
        return free(x + offsetX, y + offsetY, grid)
    }

    fun ifFreeMoveThere(offsetX: Int, offsetY: Int, grid: ArrayList<IntArray>): Boolean {
        return if (free(x + offsetX, y + offsetY, grid)) {
            moveTo(x + offsetX, y + offsetY, grid)
            true
        } else
            false
    }

    fun matchColorWithPhase(enemyPhase: Boolean) {
        if (enemyPhase) {
            normalColor = enemyNormal
            trailAnim = enemyTrail
        } else {
            normalColor = playerNormal
            trailAnim = playerTrail
        }
    }

    companion object {
        val playerNormal = GGraphics.blue.front
        val enemyNormal = GGraphics.red.front
        // whatever. If someone read this, I apologize, I'm tired, mentally and physically and don't want to try to understand why it has to be reversed
        val playerTrail = SParticles.blueAnim
        val enemyTrail = SParticles.fireAnim
        val rotationVector = Vector2()

        fun free(x: Int, y: Int, grid: ArrayList<IntArray>): Boolean {
            return getGridValue(grid, x, y) == 0
        }
        fun getGridValue(grid: ArrayList<IntArray>, x: Int, y: Int): Int {
            return if (x >= 0 && x < grid[0].size && y >= 0 && y < grid.size)
                grid[y][x]
            else
                -1
        }
    }
}