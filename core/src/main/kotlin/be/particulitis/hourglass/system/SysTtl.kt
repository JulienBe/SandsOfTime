package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompLayer
import be.particulitis.hourglass.comp.CompTtl
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysTtl : IteratingSystem(Aspect.all(CompTtl::class.java, CompLayer::class.java)) {

    private lateinit var mTtl: ComponentMapper<CompTtl>
    private lateinit var mLayer: ComponentMapper<CompLayer>

    override fun process(entityId: Int) {
        val ttl = mTtl[entityId]
        ttl.remaining -= mLayer[entityId].delta
        if (ttl.remaining < 0f) {
            ttl.finished()
            world.delete(entityId)
        }
    }

}