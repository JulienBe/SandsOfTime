package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.comp.CompTtl
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.DelayedIteratingSystem

class SysTtl : DelayedIteratingSystem(Aspect.all(CompTtl::class.java)) {
    private lateinit var mTtl: ComponentMapper<CompTtl>

    override fun getRemainingDelay(entityId: Int): Float {
        return mTtl[entityId].remaining
    }

    override fun processExpired(entityId: Int) {
        mTtl[entityId].finished()
        world.delete(entityId)
    }

    override fun processDelta(entityId: Int, accumulatedDelta: Float) {
        val ttl = mTtl[entityId]
        ttl.remaining -= accumulatedDelta
    }

}