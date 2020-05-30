package be.particulitis.hourglass.system.graphics

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.comp.CompHp
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.setups.SParticles
import com.artemis.BaseSystem
import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.math.Vector2
import ktx.collections.GdxMap
import ktx.collections.gdxMapOf
import java.util.*

class SysHpDisplay : BaseSystem() {

    private val grid = arrayListOf(
            intArrayOf(W, W, W, W, W, W, W, W),
            intArrayOf(W, W, W,0,0, W, W, W),
            intArrayOf(W, W,0,0,0,0, W, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W,0,0,0,0,0,0, W),
            intArrayOf(W, W, W, W, W, W, W, W)
    )
    private val life = gdxMapOf<Int, Drop>()
    private val height = grid.size.toFloat()
    private var nextDrop = 0f
    private val x = 30f
    private val y = 250f - height

    init {
        for (i in 0..20) {
            val drop = Drop()
            drop.updateGrid(grid)
            life.put(drop.hashCode(), drop)
        }
        center.set((grid.first().size / 2).toFloat() - 0.5f, (height / 2f) - 0.5f)
    }

    override fun processSystem() {
        val player = world.getPlayer()
        if (player == null)
            return
        val hp = player.getComponent(CompHp::class.java).hp

        if (!GGraphics.batch.isDrawing)
            GGraphics.batch.begin()
        GGraphics.batch.shader = null
        if (nextDrop < GTime.time) {
            nextDrop = GTime.time + dropFreq
            drop()
        }
        life.values().forEach {
            if (it.duplicate(grid))
                it.ifFreeMoveThere(GRand.int(-1, 1), GRand.int(-1, 1), grid, world, x, y)
        }
        if (life.size < hp) {
            val drop = Drop()
            life.put(drop.hashCode(), drop)
        } else if (life.size > hp) {
            val drop = life.get(life.keys().first())
            life.remove(drop.hashCode())
            grid[drop.y][drop.x] = 0
        }
        life.values().forEach {
            it.draw(grid, life, world, x, y)
        }
    }

    private fun drop() {
        life.values().filterNot {
            if (it.ifFreeMoveThere(0, +gravity, grid, world, x, y)) {
                true
            } else {
                // just one for the 'viscosity'
                it.ifFreeMoveThere(GRand.oneOrMinus(), gravity, grid, world, x, y)
            }
        }.forEach {
            // they have not moved
            if (GRand.nextInt(10) == 1)
                it.ifFreeMoveThere(GRand.oneOrMinus(), 0, grid, world, x, y)
            if (it.moved && !it.hasBumpedOthers &&
                    // are basically surrounded
                    !it.isFree(+0, +gravity, grid) &&
                    !it.isFree(+1, +gravity, grid) &&
                    !it.isFree(-1, +gravity, grid)) {
                it.bump()
                it.moved = false
                it.hasBumpedOthers = true
            }
        }
    }

    companion object {
        const val W = 7
        const val dropFreq = 0.024f
        const val gravity = -1
        var center = Vector2()
    }
}

private fun World.getPlayer(): Entity? {
    return getSystem(TagManager::class.java).getEntity(Data.playerTag)
}

class Drop(var x: Int = GRand.int(1, 6), var y: Int = 28) {

    private var justBumped = false
    var hasBumpedOthers = false
    var moved = false

    fun updateGrid(grid: ArrayList<IntArray>) {
        grid[y][x] = hashCode()
    }

    private fun moveTo(newX: Int, newY: Int, grid: ArrayList<IntArray>, world: World, baseX: Float, baseY: Float) {
        if (grid[y][x] == hashCode())
            grid[y][x] = 0

        SParticles.trail(world, baseX + x, baseY + y, SParticles.fireAnim, 1f)
        x = newX
        y = newY
        grid[y][x] = hashCode()
        moved = true
    }

    fun bump() {
        justBumped = true
    }

    fun draw(grid: ArrayList<IntArray>, sand: GdxMap<Int, Drop>, world: World, x: Float, y: Float) {
        if (justBumped) {
            GGraphics.batch.draw(high, x + this.x, y + this.y, 1f, 1f)
            if (GTime.alternate && GRand.bool()) {
                justBumped = false
                sand[getGridValue(grid, this.x, this.y + SysHpDisplay.gravity)]?.bump()
                sand[getGridValue(grid, this.x + 1, this.y + SysHpDisplay.gravity)]?.bump()
                sand[getGridValue(grid, this.x - 1, this.y + SysHpDisplay.gravity)]?.bump()
            }
            SParticles.trail(world, x + this.x, y + this.y, SParticles.fireAnim, 1f)
        } else {
            GGraphics.batch.draw(low, x + this.x, y + this.y, 1f, 1f)
        }
    }

    fun isFree(offsetX: Int, offsetY: Int, grid: ArrayList<IntArray>): Boolean {
        return free(x + offsetX, y + offsetY, grid)
    }

    fun ifFreeMoveThere(offsetX: Int, offsetY: Int, grid: ArrayList<IntArray>, world: World, baseX: Float, baseY: Float): Boolean {
        return if (free(x + offsetX, y + offsetY, grid)) {
            moveTo(x + offsetX, y + offsetY, grid, world, baseX, baseY)
            true
        } else
            false
    }

    fun duplicate(grid: ArrayList<IntArray>): Boolean {
       return grid[y][x] != hashCode()
    }

    companion object {
        val high = GGraphics.img("squares/square_yellow").front
        val low = GGraphics.img("squares/square_red").front

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