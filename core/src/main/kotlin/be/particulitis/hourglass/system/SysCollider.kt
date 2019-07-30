package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GAngleCollision
import be.particulitis.hourglass.common.GSide
import be.particulitis.hourglass.comp.CompCollide
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.artemis.utils.IntBag
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2

class SysCollider : BaseEntitySystem(Aspect.all(CompSpace::class.java, CompCollide::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mCollide: ComponentMapper<CompCollide>

    override fun processSystem() {
        val actives = subscription.entities
        val ids: IntArray = actives.data
        updatePos(actives, ids)
    }

    private fun updatePos(actives: IntBag, ids: IntArray) {
        for (it in actives.size() - 1 downTo 0) {
            val col = mCollide[it]
            val dim = mSpace[ids[it]]
            for (oIt in it - 1 downTo 0) {
                val oCol = mCollide[oIt]
                val oDim = mSpace[ids[oIt]]

                if (dim.rect.overlaps(oDim.rect)) {
                    val side = determineRectangleSideHit(dim.rect, oDim.rect)
                    col.setDmgToTake(oCol.dmgToInflict)
                    oCol.setDmgToTake(col.dmgToInflict)
                    pushAway(dim, side, oDim)
                }
            }
        }
    }

    private fun pushAway(dim: CompSpace, side: GSide, oDim: CompSpace, thickness: Int = 5) {
        var cpt = thickness
        do {
            dim.move(-side.x, -side.y)
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