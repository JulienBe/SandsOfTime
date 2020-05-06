package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GTime
import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import kotlin.math.min

class SysTime : BaseSystem() {

    private var currentPhaseTimer = 0f

    override fun processSystem() {
        GTime.alternate = !GTime.alternate
        GTime.delta = Gdx.graphics.deltaTime
        GTime.playerDelta = computeDeltas(GTime.enemyPhase)
        GTime.enemyDelta = computeDeltas(!GTime.enemyPhase)
        GTime.justSwitched = false

        if (currentPhaseTimer <= 0f) {
            GTime.enemyPhase = !GTime.enemyPhase
            currentPhaseTimer = phaseDuration
            GTime.phaseTime = 0f
            GTime.justSwitched = true
        }

        currentPhaseTimer -= GTime.delta
        GTime.playerTime += GTime.playerDelta
        GTime.enemyTime += GTime.enemyDelta
        GTime.phaseTime += GTime.delta
        GTime.time += GTime.delta
    }

    private fun computeDeltas(playerPhase: Boolean): Float {
        return if (playerPhase)
            0f
        else
            GTime.delta * min(1f, currentPhaseTimer)
    }

    companion object {
        const val phaseDuration = 4f
    }

}