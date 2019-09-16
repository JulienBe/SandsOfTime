package be.particulitis.hourglass.system

import com.artemis.systems.BaseSystem
import be.particulitis.hourglass.common.GTime

class SysControl : BaseSystem() {

    val phaseDuration = 3f
    var currentPhaseTimer = 0f    
    
    override fun processSystem() {
        GTime.delta = Gdx.graphics.getDeltaTime()
        currentPhaseTimer -= GTime.delta
        
        if (currentPhaseTimer <= 0f) {
            GTime.playerPhase != GTime.playerPhase
            currentPhaseTimer = phaseDuration
        }
        
        GTime.playerDelta = computeDeltas(GTime.playerPhase)
        GTime.enemyDelta = computeDeltas(!GTime.playerPhase)        
        
        GTime.playerTime += GTime.playerDelta
        GTime.enemyTime += GTime.enemyDelta
    }

    fun computeDeltas(playerPhase: Boolean): Float {
        return if (playerPhaser)
            GTime.delta * min(1f, currentPhaseTimer)
        else 
            0f
    }
}
