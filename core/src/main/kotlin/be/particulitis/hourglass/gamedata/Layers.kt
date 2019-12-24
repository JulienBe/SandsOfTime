package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.common.GTime

enum class Layers(val isPlayer: Boolean, val delta: () -> Float, val time: () -> Float) {
    Player(true, { GTime.playerDelta }, { GTime.playerTime }),
    Enemy(false, { GTime.enemyDelta },  { GTime.enemyTime }),
    Other(false, { GTime.delta },       { GTime.time })
}