package be.particulitis.hourglass.common.puppet

import be.particulitis.hourglass.common.drawing.GImage

class GAnimController(var current: GAnim) {

    fun getFrame(): GImage {
        return current.getFrame()
    }

    fun update(delta: Float) {
        current.update(delta)
    }

    fun forceCurrent(anim: GAnim) {
        current = anim
        current.start()
    }

}