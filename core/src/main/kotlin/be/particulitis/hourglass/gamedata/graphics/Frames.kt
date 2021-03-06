package be.particulitis.hourglass.gamedata.graphics

import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GImage
import be.particulitis.hourglass.common.puppet.GAnim

enum class Frames {

    PLAYER_IDLE("running_girl"),
    PLAYER_SHOOT("shooting_girl"),
    GUNNER_SPAWN("gunner_spawn"),
    GUNNER_SPAWN_PARTICLES("gunner_spawn_particles"),
    GUNNER_ROTATE("gunner_rotate"),
    GUNNER_SHOOT("gunner_shoot"),
    CPU_SPAWN("cpu_spawn"),
    CPU_IDLE("cpu"),
    CPU_WALK("cpu_walk"),
    CPU_JUMPING("cpu_jumping"),
    CPU_LAND("cpu_land"),
    FIRE    ("squares/square_yellow", "squares/square_orange", "squares/square_red"),
    BLUE    ("squares/square_dark_purple", "squares/square_dark_blue", "squares/square_blue"),
    PINK    ("squares/square_pink_skin", "squares/square_pink", "squares/square_red"),
    LAVENDER("squares/square_lavender", "squares/square_dark_purple", "squares/square_dark_blue"),
    WHITE   ("squares/square_white", "squares/square_light_grey", "squares/square_dark_grey")
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
            if (GGraphics.assMan.regions.containsKey("${name}_f$i")) {
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