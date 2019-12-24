package be.particulitis.hourglass.comp

import be.particulitis.hourglass.gamedata.Layers

class CompLayer : Comp() {

    var layer = Layers.Player
    private set
    val delta: Float
        get() { return layer.delta.invoke() }
    val time: Float
        get() { return layer.time.invoke() }
    val isPlayer: Boolean
        get() { return layer.isPlayer }

    fun setLayer(layer: Layers) {
        this.layer = layer
    }

}