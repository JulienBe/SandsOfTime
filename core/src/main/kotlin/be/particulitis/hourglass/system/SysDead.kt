package be.particulitis.hourglass.system

import be.particulitis.hourglass.gamedata.Setup
import be.particulitis.hourglass.comp.CompHp
import be.particulitis.hourglass.states.StateManager
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager
import com.artemis.systems.IteratingSystem

class SysDead : IteratingSystem(Aspect.all(CompHp::class.java)) {

    private lateinit var mHp: ComponentMapper<CompHp>

    override fun process(entityId: Int) {
        val hp = mHp[entityId]

        if (hp.hp <= 0) {
            world.delete(entityId)
            if (world.getSystem(TagManager::class.java).getTag(entityId) == Setup.playerTag)
                StateManager.playerDead(world)
        }
    }
}