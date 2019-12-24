package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompParticle
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem

@Wire(failOnNull = false)
class SysParticle : IteratingSystem(Aspect.all(CompParticle::class.java)) {

    private lateinit var mParticle: ComponentMapper<CompParticle>

    override fun process(entityId: Int) {
        mParticle[entityId].update.invoke()
    }

}