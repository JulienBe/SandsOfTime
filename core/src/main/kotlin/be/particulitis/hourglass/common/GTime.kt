package be.particulitis.hourglass.common

object GTime {

    var enemyPhase = true
    var justSwitched = false
    var delta = 0f
    var playerDelta = 0f
    var enemyDelta = 0f
    var time = 0f
    var playerTime = 0f
    var enemyTime = 0f
    var phaseTime = 0f
    var alternate = true

    fun reset() {
        enemyPhase = true
        delta = 0f
        playerDelta = 0f
        enemyDelta = 0f
        time = 0f
        playerTime = 0f
        enemyTime = 0f
        phaseTime = 0f
    }

    fun myTime(isPlayer: Boolean): Float {
        return if (isPlayer)
            playerTime
        else
            enemyTime
    }
}
