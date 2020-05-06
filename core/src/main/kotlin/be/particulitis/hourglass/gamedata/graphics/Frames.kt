package be.particulitis.hourglass.gamedata.graphics

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GImage
import be.particulitis.hourglass.common.puppet.GAnim

enum class Frames {

    PLAYER_IDLE("running_girl"),
    PLAYER_SHOOT("shooting_girl"),
    CPU_SPAWN("cpu_spawn"),
    CPU_IDLE("cpu"),
    CPU_WALK("cpu_walk"),
    CPU_JUMPING("cpu_jumping"),
    FIRE("squares/square_yellow", "squares/square_orange", "squares/square_red"),
    BLUE("squares/square_dark_purple", "squares/square_dark_blue", "squares/square_blue"),
    PINK("squares/square_pink_skin", "squares/square_pink", "squares/square_red")
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
        for (i in 1..GAnim.hugeMaxFrameNumberThatWillNeverEverBeReachedByOneAnimation) {
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
            deBus.add(GImage(it))
        }
        frames = deBus.toTypedArray()
        println("Frames: ${frames.size} $name")
    }

}