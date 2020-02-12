package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompHp
import be.particulitis.hourglass.comp.CompParticleEmitter
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.states.StateManager
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import com.artemis.systems.IteratingSystem

class SysDead : IteratingSystem(Aspect.all(CompHp::class.java)) {

    private lateinit var mHp: ComponentMapper<CompHp>
    private lateinit var mEmitter: ComponentMapper<CompParticleEmitter>

    override fun process(entityId: Int) {
        val hp = mHp[entityId]

        if (hp.hp <= 0) {
            hp.onDead.invoke()
            world.delete(entityId)
            if (world.getSystem(TagManager::class.java).getTag(entityId) == Data.playerTag)
                StateManager.playerDead(world)
            if (mEmitter.has(entityId))
                mEmitter[entityId].emit.invoke()
        }
    }
}