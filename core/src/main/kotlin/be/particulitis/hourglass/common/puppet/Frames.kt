package be.particulitis.hourglass.common.puppet

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GImage

enum class Frames {

    PLAYER_IDLE("running_girl"),
    PLAYER_SHOOT("running_girl"),
    CPU_SPAWN("cpu_spawn"),
    CPU_IDLE("cpu"),
    CPU_WALK("cpu_walk"),
    CPU_JUMPING("cpu_jumping"),
    CPU_ATTACK("cpu_attack"),
    FIRE("squares/square_red", "squares/square_orange", "squares/square_yellow"),
    BLUE("squares/square_purple", "squares/square_blue", "squares/square_cyan"),
    PINK("squares/square_red", "squares/square_pink", "squares/square_pink_skin")
    ;

    val size: Int get() = frames.size
    val frames: Array<GImage>

    operator fun get(currentFrame: Int): GImage {
        return frames[currentFrame]
    }

    fun forEach(function: () -> Unit) {
        frames.forEach { _ -> function.invoke() }
    }

    constructor(name: String) {
        val desFesses = mutableListOf<GImage>()
        for (i in 1..GAnimN.hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation) {
            if (GGraphics.imgMan.regions.containsKey("${name}_f$i")) {
                desFesses.add(GImage("${name}_f$i"))
            } else
                break
        }
        frames = desFesses.toTypedArray()
    }
    constructor(vararg names: String) {
        val deBus = mutableListOf<GImage>()
        names.forEach {
            println("create img with $it")
            deBus.add(GImage(it))
        }
        frames = deBus.toTypedArray()
    }

}