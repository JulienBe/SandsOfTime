package be.particulitis.hourglass.common.puppet

import be.particulitis.hourglass.common.drawing.GImage

class GFrame(val frameTime: Float, val img: GImage) {

    var onFrame = {}
    var inFrame = {}
    var time = 0f

}