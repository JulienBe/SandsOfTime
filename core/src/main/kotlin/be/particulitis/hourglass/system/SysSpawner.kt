package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompEnemy
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.BaseSystem
import com.artemis.ComponentMapper
import com.artemis.managers.TagManager

class SysSpawner : BaseSystem() {

    private lateinit var mSpace: ComponentMapper<CompSpace>

    override fun processSystem() {
        val enemies = world.aspectSubscriptionManager.get(Aspect.all(CompEnemy::class.java))
        if (enemies.entities.size() < min + (GTime.playerTime / 2f)) {
            val enemyEntityId = FirstScreen.world.create(Builder.enemy.build(FirstScreen.world))
            val playerPos = mSpace[world.getSystem(TagManager::class.java).getEntity(Setup.playerTag)]
            Setup.enemy(enemyEntityId, FirstScreen.world, playerPos.centerX - exclusionRange, playerPos.centerX + exclusionRange, playerPos.centerY - exclusionRange, playerPos.centerY + exclusionRange)
        }
    }

    companion object {
        val min = 5
        val exclusionRange = GResolution.areaDim / 5f
    }
}