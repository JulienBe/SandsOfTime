package be.particulitis.hourglass.common

import be.particulitis.hourglass.common.drawing.GGraphics
import com.badlogic.gdx.Gdx

class GMusic(val name: String) {
    private val audioFile = GGraphics.assMan.music(name)
    private val bpm = Gdx.files.internal("processedmusic/${name}.bpm")
            .readString()
            .split("\n")
            .filter { it.isNotEmpty() && it.isNotBlank() }
            .map { Pair(it.toFloat(), 0f) }
    private val percussions = Gdx.files.internal("processedmusic/${name}.percussions")
            .readString()
            .split("\n")
            .filter { it.isNotEmpty() && it.isNotBlank() }
            .map {
                val splitted = it.split(",")
                Pair(splitted[0].toFloat(), splitted[1].toFloat())
            }
    private var bpmIndex = Index()
    private var percussionIndex = Index()

    fun play() {
        audioFile.play()
        audioFile.volume = 0.5f
        audioFile.isLooping = true
        bpmIndex.reset()
        percussionIndex.reset()
    }

    fun currentHigh(): Float {
        mutateIndex(percussionIndex, percussions)
        return percussions[percussionIndex.i].second
    }

    fun newBpm(): Boolean {
        mutateIndex(bpmIndex, bpm)
        return bpmIndex.justChanged
    }

    private fun mutateIndex(index: Index, ref: List<Pair<Float, Float>>): Index {
        index.justChanged = false
        // when you loop and the framerate doesn't keep up with the timeserie, you can miss the end / reboot
        if (audioFile.position < ref[index.i].first) {
            index.i = 0
        }
        if (index.i + 1 < ref.size && audioFile.position >= ref[index.i + 1].first) {
            index.justChanged = true
            index.i += 1
            if (index.i == ref.size) {
                index.i = 0
            }
        }
        return index
    }

    fun stop() {
        audioFile.stop()
    }

}

data class Index(var i: Int = 0, var justChanged: Boolean = false) {
    fun reset() {
        i = 0
        justChanged = false
    }
}