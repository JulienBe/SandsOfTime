package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.common.drawing.GGraphics

enum class Dim(val w: Float, val h: Float = w) {
    Player(6f),
    PlayerSprite(GGraphics.tr(ImgMan.player + "1").regionWidth.toFloat(), GGraphics.tr(ImgMan.player + "1").regionHeight.toFloat()),
    CpuSprite(GGraphics.tr(ImgMan.player + "1").regionWidth.toFloat(), GGraphics.tr(ImgMan.player + "1").regionHeight.toFloat()),
    WallSprite(GGraphics.tr(ImgMan.wall).regionWidth.toFloat(), GGraphics.tr(ImgMan.wall).regionHeight.toFloat()),
    Enemy(12f),
    Bullet(2f);

    val hw = w / 2f
    val hh = h / 2f
    val tw = w / 3f
    val th = h / 3f

}