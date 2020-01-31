package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GAngleCollision
import be.particulitis.hourglass.common.GSide
import be.particulitis.hourglass.comp.CompCollide
import be.particulitis.hourglass.comp.CompDir
import be.particulitis.hourglass.comp.CompSide
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.utils.IntBag
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs
import kotlin.math.atan2

class SysCollider : BaseEntitySystem(Aspect.all(CompSpace::class.java, CompCollide::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mCollide: ComponentMapper<CompCollide>
    private lateinit var mDir: ComponentMapper<CompDir>
    private lateinit var mSide: ComponentMapper<CompSide>

    override fun processSystem() {
        val actives = subscription.entities
        val ids: IntArray = actives.data
        updatePos(actives, ids)
    }

    private fun updatePos(actives: IntBag, ids: IntArray) {
        for (it in actives.size() - 1 downTo 0) {
            val col = mCollide[ids[it]]
            val space = mSpace[ids[it]]
            for (oIt in it - 1 downTo 0) {
                val oCol = mCollide[ids[oIt]]
                val oSpace = mSpace[ids[oIt]]

                if ((oCol.id and col.collidesWith != 0) && space.rect.overlaps(oSpace.rect)) {
                    val side = determineRectangleSideHit(space.rect, oSpace.rect)
                    oCol.dmgToTake
                    col.setDmgToTake(oCol.dmgToInflict)
                    oCol.setDmgToTake(col.dmgToInflict)
//                    pushAway(space, side, oSpace)
                    oCol.collision.invoke(col, space, side)
                    col.collision.invoke(oCol, oSpace, side)
                    col.collidingMap[oCol.id].invoke(ids[it], ids[oIt], col, space, oCol, oSpace, side)
                }
            }
        }
    }

    private fun pushAway(dim: CompSpace, side: GSide, oDim: CompSpace, thickness: Int = 5) {
        var cpt = thickness
        do {
            dim.move(-side.pushX, -side.pushY, 1f)
        } while (cpt-- > 0 && dim.rect.overlaps(oDim.rect))
    }

    fun determineRectangleSideHit(rect: Rectangle, other: Rectangle): GSide {
        val angle = angle(
                (rect.x - rect.width / 2) - ((other.x - other.width / 2)),
                (rect.y - rect.height / 2) - (other.y - other.height / 2)
        )
        collisionAngles.forEach {
            if (angle > it.min && angle <= it.max)
                return it.side
        }
        return collisionAngles.last().side
    }

    fun bounce(entity: Int, otherEntity: Int, meCollide: CompCollide, meSpace: CompSpace, otherCollide: CompCollide, otherSpace: CompSpace, side: GSide) {
        val meDir = mDir[entity]

        if (side == GSide.TOP || side == GSide.BOTTOM) {
            meDir.set(meDir.x, abs(meDir.y) * side.pushY)
        } else {
            meDir.set(abs(meDir.x) * side.pushX, meDir.y)
        }
    }
    fun bounceOfWall(entity: Int, otherEntity: Int, meCollide: CompCollide, meSpace: CompSpace, otherCollide: CompCollide, otherSpace: CompSpace, side: GSide) {
        bounce(entity, otherEntity, meCollide, meSpace, otherCollide, otherSpace, mSide[otherEntity].side)
    }
    fun rollback(entity: Int, otherEntity: Int, meCollide: CompCollide, meSpace: CompSpace, otherCollide: CompCollide, otherSpace: CompSpace, side: GSide) {
        meSpace.rollback()
    }

    companion object {
        val center = Vector2(5f, 5f)
        val topRight = Vector2(10f, 10f)
        val bottomRight = Vector2(10f, 0f)
        val bottomLeft = Vector2(0f, 0f)
        val topLeft = Vector2(0f, 10f)
        val collisionAngles = arrayListOf(
                GAngleCollision(angle(center.x - topRight.x, center.y - topRight.y), angle(center.x - topLeft.x, center.y - topLeft.y), GSide.TOP),
                GAngleCollision(angle(center.x - bottomRight.x, center.y - bottomRight.y), angle(center.x - bottomLeft.x, center.y - bottomLeft.y), GSide.BOTTOM),
                GAngleCollision(angle(center.x - topRight.x, center.y - topRight.y), angle(center.x - bottomRight.x, center.y - bottomRight.y), GSide.RIGHT),
                GAngleCollision(angle(center.x - topLeft.x, center.y - topLeft.y), angle(center.x - bottomLeft.x, center.y - bottomLeft.y), GSide.LEFT)
        )

        fun angle(x: Float, y: Float): Float {
            var angle = atan2(y.toDouble(), x.toDouble()).toFloat() * MathUtils.radiansToDegrees
            if (angle < 0) angle += 360f
            return angle
        }
    }
}