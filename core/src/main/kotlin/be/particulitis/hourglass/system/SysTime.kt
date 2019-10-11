package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GTime
import com.artemis.BaseSystem
import com.badlogic.gdx.Gdx
import kotlin.math.min

class SysTime : BaseSystem() {

    val phaseDuration = 3f
    var currentPhaseTimer = 0f    
    
    override fun processSystem() {
        GTime.delta = Gdx.graphics.deltaTime
        currentPhaseTimer -= GTime.delta
        
        if (currentPhaseTimer <= 0f) {
            GTime.playerPhase = !GTime.playerPhase
            currentPhaseTimer = phaseDuration
        }

        GTime.playerDelta = computeDeltas(GTime.playerPhase)
        GTime.enemyDelta = computeDeltas(!GTime.playerPhase)        
        
        GTime.playerTime += GTime.playerDelta
        GTime.enemyTime += GTime.enemyDelta
    }

    fun computeDeltas(playerPhase: Boolean): Float {
        return if (playerPhase)
            0f
        else
            GTime.delta * min(1f, currentPhaseTimer)
    }
}
