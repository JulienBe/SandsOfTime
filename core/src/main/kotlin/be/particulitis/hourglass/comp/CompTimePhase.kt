package be.particulitis.hourglass.comp

import be.particulitis.hourglass.gamedata.Phases

class CompTimePhase : Comp() {

    var layer = Phases.Player
    private set
    val delta: Float
        get() { return layer.delta.invoke() }
    val time: Float
        get() { return layer.time.invoke() }
    val isPlayer: Boolean
        get() { return layer.isPlayer }

    fun setLayer(layer: Phases) {
        this.layer = layer
    }

}