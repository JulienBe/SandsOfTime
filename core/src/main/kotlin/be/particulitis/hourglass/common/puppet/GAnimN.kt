package be.particulitis.hourglass.common.puppet

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GImage
import com.badlogic.gdx.graphics.g2d.Animation
import ktx.collections.GdxArray

class GAnimN(name: String, var playMode: Animation.PlayMode = Animation.PlayMode.LOOP) {

    private var totalTime = 0f
    private var frameTime = 0f
    private var currentFrame = 0
    val frames = GdxArray<GFrame>()
    var time = 0f
    var onStart = {}

    init {
        for (i in 1..hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation) {
            if (GGraphics.imgMan.regions.containsKey("${name}_f$i")) {
                frames.add(GFrame(0.1f, GImage("${name}_f$i")))
                totalTime += 0.1f
            } else
                break
        }
    }

    fun getFrame(): GImage {
        return frames[currentFrame].img
    }

    fun update(delta: Float) {
        time += delta
        frameTime += delta
        if (frameTime > frames[currentFrame].frameTime)
            nextFrame()
        frames[currentFrame].inFrame.invoke()
    }

    fun nextFrame() {
        frameTime = 0f
        currentFrame++
        if (currentFrame >= frames.size) {
            when (playMode) {
                Animation.PlayMode.LOOP -> currentFrame = 0
                else -> currentFrame = frames.size - 1
            }
        }
        frames[currentFrame].onFrame.invoke()
    }

    fun start() {
        time = 0f
        frameTime = 0f
        currentFrame = 0
        onStart.invoke()
    }

    fun inEachFrame(function: () -> Unit) {
        frames.forEach {
            it.inFrame = function
        }
    }

    companion object {
        const val hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation = 100
    }
}