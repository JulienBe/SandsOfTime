package be.particulitis.hourglass.common.puppet

import be.particulitis.hourglass.common.drawing.GImage

class GAnimController(var current: GAnimN) {

    fun getFrame(): GImage {
        return current.getFrame()
    }

    fun update(delta: Float) {
        current.update(delta)
    }

    fun forceCurrent(anim: GAnimN) {
        current = anim
        current.start()
    }

}