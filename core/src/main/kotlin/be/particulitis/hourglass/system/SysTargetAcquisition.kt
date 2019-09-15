package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.*
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysTargetAcquisition : IteratingSystem(Aspect.all(CompTargetSeek::class.java, CompTargetFollow::class.java)) {

    private lateinit var mTarget: ComponentMapper<CompTargetSeek>
    private lateinit var mFollow: ComponentMapper<CompTargetFollow>

    override fun process(entityId: Int) {
        val target = mTarget[entityId]
        val follow = mFollow[entityId]

        if (follow.target.generation == follow.gen) {
            target.set(follow.target.centerX, follow.target.centerY)
        }
    }
}