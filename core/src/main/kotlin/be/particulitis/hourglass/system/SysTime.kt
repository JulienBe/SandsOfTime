package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GTime
import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import kotlin.math.min

class SysTime : BaseSystem() {

    private val phaseDuration = 300f
    private var currentPhaseTimer = 0f

    override fun processSystem() {
        GTime.playerDelta = computeDeltas(GTime.enemyPhase)
        GTime.enemyDelta = computeDeltas(!GTime.enemyPhase)

        if (currentPhaseTimer <= 0f) {
            GTime.enemyPhase = !GTime.enemyPhase
            currentPhaseTimer = phaseDuration
        }

        GTime.delta = Gdx.graphics.deltaTime
        currentPhaseTimer -= GTime.delta
        GTime.playerTime += GTime.playerDelta
        GTime.enemyTime += GTime.enemyDelta
        GTime.time += GTime.delta
    }

    private fun computeDeltas(playerPhase: Boolean): Float {
        return if (playerPhase)
            0f
        else
            GTime.delta * min(1f, currentPhaseTimer)
    }

}