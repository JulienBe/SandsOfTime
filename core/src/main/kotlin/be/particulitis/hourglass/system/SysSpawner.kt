package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.builds.Setup
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompEnemy
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.*
import com.artemis.managers.TagManager
import kotlin.reflect.KFunction6

class SysSpawner : BaseSystem() {

    private lateinit var mSpace: ComponentMapper<CompSpace>

    override fun processSystem() {
        val enemies = world.aspectSubscriptionManager.get(Aspect.all(CompEnemy::class.java))
        if (enemies.entities.size() < min + (GTime.playerTime / 2f)) {
            val playerPos = mSpace[world.getSystem(TagManager::class.java).getEntity(Setup.playerTag)]
            if (GRand.nextBoolean())
                addEnemy(Builder.enemySlug, playerPos, Setup::enemySlug)
            else
                addEnemy(Builder.enemyShoot, playerPos, Setup::enemyShoot)
        }
    }

    private fun addEnemy(builder: ArchetypeBuilder, playerPos: CompSpace, setup: KFunction6<@ParameterName(name = "id") Int, @ParameterName(name = "world") World, @ParameterName(name = "exclusionStartX") Float, @ParameterName(name = "exclusionStopX") Float, @ParameterName(name = "exclusionStartY") Float, @ParameterName(name = "exclusionStopY") Float, Unit>) {
        val enemyEntityId = FirstScreen.world.create(builder.build(FirstScreen.world))
        setup.invoke(enemyEntityId, FirstScreen.world, playerPos.centerX - exclusionRange, playerPos.centerX + exclusionRange, playerPos.centerY - exclusionRange, playerPos.centerY + exclusionRange)
    }

    companion object {
        const val min = 5
        const val exclusionRange = GResolution.areaDim / 5f
    }
}