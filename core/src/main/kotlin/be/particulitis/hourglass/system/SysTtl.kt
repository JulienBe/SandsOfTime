package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GInput
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompIsPlayer
import be.particulitis.hourglass.comp.CompTtl
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.DelayedIteratingSystem
import com.artemis.systems.IteratingSystem

class SysTtl : IteratingSystem(Aspect.all(CompTtl::class.java, CompIsPlayer::class.java)) {

    private lateinit var mTtl: ComponentMapper<CompTtl>
    private lateinit var mPlayer: ComponentMapper<CompIsPlayer>

    override fun process(entityId: Int) {
        val ttl = mTtl[entityId]
        if (mPlayer[entityId].isPlayer)
            ttl.remaining -= GTime.playerDelta
        else
            ttl.remaining -= GTime.enemyDelta
        if (ttl.remaining < 0f) {
            ttl.finished()
            world.delete(entityId)
        }
    }

}