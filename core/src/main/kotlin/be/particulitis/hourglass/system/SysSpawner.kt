package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import be.particulitis.hourglass.comp.CompEnemy
import com.artemis.Aspect
import com.artemis.BaseSystem
import com.artemis.ComponentMapper

class SysSpawner : BaseSystem() {

    private lateinit var mEnemy: ComponentMapper<CompEnemy>

    override fun processSystem() {
        val enemies = world.aspectSubscriptionManager.get(Aspect.all(CompEnemy::class.java))
        if (enemies.entities.size() < min) {
            val enemyEntityId = FirstScreen.world.create(Builder.enemy.build(FirstScreen.world))
            Setup.enemy(enemyEntityId, FirstScreen.world)
        }
    }

    companion object {
        val min = 5
    }
}