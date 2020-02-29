package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompEnemy
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.setups.SEnemy
import be.particulitis.hourglass.screens.GameScreen
import com.artemis.*
import com.artemis.managers.TagManager
import kotlin.reflect.KFunction6

class SysSpawner : BaseSystem() {

    private lateinit var mSpace: ComponentMapper<CompSpace>

    override fun processSystem() {
        val enemies = world.aspectSubscriptionManager.get(Aspect.all(CompEnemy::class.java))
        if (enemies.entities.size() < min + (GTime.playerTime / 4f)) {
            val playerPos = mSpace[world.getSystem(TagManager::class.java).getEntity(Data.playerTag)]
            SEnemy.enemySlug(world, GRand.float(30f, GResolution.areaW - 30f), GRand.float(30f, GResolution.areaH - 30f))
        }
    }

    private fun addEnemy(builder: ArchetypeBuilder, playerPos: CompSpace, setup: KFunction6<@ParameterName(name = "id") Int, @ParameterName(name = "world") World, @ParameterName(name = "exclusionStartX") Float, @ParameterName(name = "exclusionStopX") Float, @ParameterName(name = "exclusionStartY") Float, @ParameterName(name = "exclusionStopY") Float, Unit>) {
        val enemyEntityId = GameScreen.world.create(builder.build(GameScreen.world))
        setup.invoke(enemyEntityId, GameScreen.world, playerPos.centerX - exclusionRange, playerPos.centerX + exclusionRange, playerPos.centerY - exclusionRange, playerPos.centerY + exclusionRange)
    }

    companion object {
        const val min = 5
        const val exclusionRange = GResolution.areaH / 5f
    }
}