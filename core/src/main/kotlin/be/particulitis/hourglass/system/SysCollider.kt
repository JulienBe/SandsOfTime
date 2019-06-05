package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompCollide
import be.particulitis.hourglass.comp.CompPos
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper

class SysCollider : BaseEntitySystem(Aspect.all(CompPos::class.java, CompCollide::class.java)) {

    private lateinit var mPos: ComponentMapper<CompPos>
    private lateinit var mCollide: ComponentMapper<CompCollide>

    override fun processSystem() {
        val actives = subscription.entities
        val ids: IntArray = actives.data
        for (it in 0 until actives.size()) {
            val pos = mPos[ids[it]]
            for (otherIt in it + 1 until actives.size()) {
                val otherPos = mPos[ids[otherIt]]
                if (collide(pos, otherPos)) {
                    val colliderA = mCollide[ids[otherIt]]
                    val colliderB = mCollide[ids[it]]
                    colliderA.collidesWith(colliderB)
                    colliderB.collidesWith(colliderA)
                }
            }
        }
    }

    private fun collide(a: CompPos, b: CompPos): Boolean {
        return a.x < b.x + b.w && a.x + a.w > b.x && a.y < b.y + b.h && a.y + a.h > b.y
    }

}