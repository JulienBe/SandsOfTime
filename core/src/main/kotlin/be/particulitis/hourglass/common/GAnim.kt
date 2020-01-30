package be.particulitis.hourglass.common

import be.particulitis.hourglass.common.drawing.GGraphics
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import ktx.collections.GdxArray

class GAnim(val name: String, val frameTime: Float) {

    private val frames = GdxArray<TextureRegion>()
    private var totalTime = 0f
    var time = 0f
    var playMode = Animation.PlayMode.NORMAL
    val isFinished: Boolean
        get() = time > totalTime

    init {
        for (i in 1..hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation) {
            if (GGraphics.imgMan.regions.containsKey(name + i)) {
                frames.add(GGraphics.tr(name + i))
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

    fun frame(delta: Float): TextureRegion {
        time += delta
        return getKeyFrame()
    }

    private fun getKeyFrame(): TextureRegion {
        return frames[MathUtils.clamp((time / frameTime).toInt(), 0, frames.size - 1)]
    }

    fun nextAnim(anim: GAnim): GAnim {
        return if (isFinished)
            anim.start()
        else
            this
    }

    fun finish() {
        time = totalTime + 1f
    }

    companion object {
        const val hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation = 100
    }
}
