package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompTimePhase
import be.particulitis.hourglass.comp.CompTtl
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysTtl : IteratingSystem(Aspect.all(CompTtl::class.java, CompTimePhase::class.java)) {

    private lateinit var mTtl: ComponentMapper<CompTtl>
    private lateinit var mTimePhase: ComponentMapper<CompTimePhase>

    override fun process(entityId: Int) {
        val ttl = mTtl[entityId]
        ttl.remaining -= mTimePhase[entityId].delta
        if (ttl.remaining < 0f) {
            ttl.onEnd.invoke()
            world.delete(entityId)
        }
    }

}