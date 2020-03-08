package be.particulitis.hourglass.common.puppet

import be.particulitis.hourglass.common.drawing.GImage
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxArray

class GAnimN(private val frames: Frames, var playMode: Animation.PlayMode = Animation.PlayMode.LOOP) {

    private var frameTime = 0f
    private var frameExpiration = 0.1f
    private var currentFrame = 0
    var time = 0f
    var onStart = {}
    val inFrame = GdxArray<() -> Unit>(frames.size)
    val onFrame = GdxArray<() -> Unit>(frames.size)
    val totalTime = frames.size * frameExpiration

    init {
        for (i in 0 until frames.size) {
            inFrame.add {}
            onFrame.add {}
        }
    }

    fun getFrame(): GImage {
        return frames[currentFrame]
    }

    fun update(delta: Float): GImage {
        time += delta
        frameTime += delta
        if (frameTime > frameExpiration)
            nextFrame()
        inFrame[currentFrame].invoke()
        return getFrame()
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
        onFrame[currentFrame].invoke()
    }

    fun start() {
        time = 0f
        frameTime = 0f
        currentFrame = 0
        onStart.invoke()
    }

    fun inEachFrame(function: () -> Unit) {
        for (i in 0 until inFrame.size)
            inFrame[i] = function
    }

    fun lastInFunction(function: () -> Unit) {
        inFrame[inFrame.size - 1] = function
    }
    fun lastOnFunction(function: () -> Unit) {
        onFrame[onFrame.size - 1] = function
    }
    fun isFinished(time: Float): Boolean {
        return totalTime <= time
    }
    fun getKeyFrame(t: Float): GImage {
        return frames[MathUtils.clamp((t / frameTime).toInt(), 0, frames.size - 1)]
    }

    companion object {
        const val hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation = 100
    }
}