package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.AssMan
import be.particulitis.hourglass.common.drawing.GGraphics

enum class Dim(val w: Float, val h: Float = w) {
    Player(8f),
    PlayerSprite(8f),
    WallSprite(GGraphics.tr(AssMan.wall).regionWidth.toFloat(), GGraphics.tr(AssMan.wall).regionHeight.toFloat()),
    Enemy(12f),
    Bullet(2f);

    val hw = w / 2f
    val hh = h / 2f
    val tw = w / 3f
    val th = h / 3f

}