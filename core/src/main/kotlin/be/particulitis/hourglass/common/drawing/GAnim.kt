package be.particulitis.hourglass.common.drawing

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxArray

class GAnim(val name: String, val frameTime: Float) {

    private val frames = GdxArray<GImage>()
    private var totalTime = 0f
    var time = 0f
    var playMode = Animation.PlayMode.NORMAL
    val isFinished: Boolean
        get() = time > totalTime

    init {
        for (i in 1..hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation) {
            if (GGraphics.imgMan.regions.containsKey(name + i)) {
                frames.add(
                        GImage(GGraphics.tr(name + i), GGraphics.nor(name + i), GGraphics.occ(name + i))
                )
            } else {
                break
            }
        }
        totalTime = frames.size * frameTime
    }

    fun start(): GAnim {
        time = 0f
        return this
    }

    fun frame(delta: Float): GImage {
        time += delta
        return getKeyFrame()
    }


    fun getKeyFrame(t: Float = time): GImage {
        return frames[MathUtils.clamp((t / frameTime).toInt(), 0, frames.size - 1)]
    }

    fun finish() {
        time = totalTime + 1f
    }

    fun addFrame(img: GImage) {
        frames.add(img)
        totalTime = frames.size * frameTime
    }

    companion object {
        const val hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation = 100
    }
}
