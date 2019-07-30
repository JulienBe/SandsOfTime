package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompDimension
import be.particulitis.hourglass.comp.CompDraw
import be.particulitis.hourglass.comp.CompHp
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysDead : IteratingSystem(Aspect.all(CompHp::class.java)) {

    private lateinit var mHp: ComponentMapper<CompHp>

    override fun process(entityId: Int) {
        val hp = mHp[entityId]

        if (hp.hp <= 0) {
            println("delete $entityId with hp ${hp.hp}")
            world.delete(entityId)
        }
    }
}