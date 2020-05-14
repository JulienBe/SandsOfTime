package be.particulitis.hourglass.common.drawing

enum class GShade(val lights: ArrayList<GPalette>, bright: Int, dark: Int) {
    BLACK(arrayListOf(      GPalette.BLACK,         GPalette.BLACK,         GPalette.BLACK,         GPalette.BLACK,         GPalette.BLACK,     GPalette.BLACK), 0, 5),
    DARK_BLUE(arrayListOf(  GPalette.DARK_BLUE,        GPalette.DARK_BLUE,        GPalette.DARK_BLUE,        GPalette.BLACK,         GPalette.BLACK,     GPalette.BLACK), 5, 0),
    DARK_PINK(arrayListOf(  GPalette.DARK_PURPLE,     GPalette.DARK_PURPLE,     GPalette.DARK_BLUE,        GPalette.DARK_BLUE,        GPalette.BLACK,     GPalette.BLACK), 0, 3),
    DARK_GREEN(arrayListOf( GPalette.DARK_GREEN,    GPalette.DARK_GREEN,    GPalette.DARK_GREEN,    GPalette.DARK_BLUE,        GPalette.DARK_BLUE, GPalette.BLACK), 0, 3),
    BROWN(arrayListOf(      GPalette.BROWN,         GPalette.DARK_PURPLE,     GPalette.DARK_PURPLE,     GPalette.DARK_BLUE,        GPalette.DARK_BLUE, GPalette.BLACK), 0, 4),
    DARK_GRAY(arrayListOf(  GPalette.DARK_GREY,        GPalette.DARK_GREY,        GPalette.DARK_BLUE,        GPalette.DARK_BLUE,        GPalette.DARK_BLUE, GPalette.BLACK), 0, 2),
    LIGHT_GRAY(arrayListOf( GPalette.LIGHT_GREY,        GPalette.LIGHT_GREY,        GPalette.LAVENDER,  GPalette.DARK_GREY,        GPalette.DARK_BLUE, GPalette.BLACK), 0, 3),
    WHITEISH(arrayListOf(   GPalette.WHITE,      GPalette.WHITE,      GPalette.LIGHT_GREY,        GPalette.LAVENDER,  GPalette.DARK_BLUE, GPalette.BLACK), 0, 3),
    REDISH(arrayListOf(     GPalette.RED,        GPalette.RED,        GPalette.DARK_PURPLE,     GPalette.DARK_PURPLE,     GPalette.DARK_BLUE, GPalette.BLACK), 0, 3),
    ORANGE(arrayListOf(     GPalette.ORANGE,        GPalette.BROWN,         GPalette.DARK_PURPLE,     GPalette.DARK_PURPLE,     GPalette.DARK_BLUE, GPalette.BLACK), 0, 1),
    YELLOW(arrayListOf(     GPalette.YELLOW,        GPalette.ORANGE,        GPalette.BROWN,         GPalette.DARK_PURPLE,     GPalette.DARK_BLUE, GPalette.BLACK), 0, 1),
    GREEN(arrayListOf(      GPalette.GREEN,         GPalette.GREEN,         GPalette.DARK_GREEN,    GPalette.DARK_GREEN,    GPalette.DARK_BLUE, GPalette.BLACK), 0, 3),
    BLUE(arrayListOf(       GPalette.BLUE,          GPalette.BLUE,          GPalette.LAVENDER,  GPalette.BROWN,         GPalette.DARK_BLUE, GPalette.BLACK), 0, 2),
    LIGHT_PURPLE(arrayListOf(GPalette.LAVENDER, GPalette.LAVENDER,  GPalette.DARK_GREY,        GPalette.DARK_BLUE,        GPalette.DARK_BLUE, GPalette.BLACK), 0, 3),
    PINK(arrayListOf(       GPalette.PINK,          GPalette.PINK,          GPalette.BROWN,         GPalette.DARK_PURPLE,     GPalette.DARK_BLUE, GPalette.BLACK), 0, 3),
    PINK_SKIN(arrayListOf(  GPalette.PINK_SKIN,     GPalette.ORANGE,        GPalette.BROWN,         GPalette.DARK_PURPLE,     GPalette.DARK_BLUE, GPalette.BLACK), 0, 2);

    val low: GPalette = lights[bright]
    val high: GPalette = lights[dark]

    companion object {
        val lightLevels = values().first().lights.size
        val fonts = arrayListOf(
                DARK_BLUE, DARK_PINK, DARK_GREEN, BROWN, DARK_GRAY, LIGHT_GRAY, WHITEISH, REDISH, ORANGE, YELLOW, GREEN, BLUE, LIGHT_PURPLE, PINK, PINK_SKIN
        )
        fun rand(): GShade {
            return values().random()
        }
        fun randFont(): GShade {
            return fonts.random()
        }

    }
}