package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.common.GDir
import be.particulitis.hourglass.gamedata.graphics.Anims33

object Data {

    const val playerBulletLayer = 9
    const val enemyBulletLayer = 2
    const val enemyLayer = 1

    const val playerTag = "PLAYER"

    val shootAnims = mapOf(
            GDir.None to Anims33.SquareNoDir,
            GDir.Right to Anims33.ShootFromRight,
            GDir.DownRight to Anims33.ShootFromDownRight,
            GDir.Down to Anims33.ShootFromDown,
            GDir.DownLeft to Anims33.ShootFromDownLeft,
            GDir.Left to Anims33.ShootFromLeft,
            GDir.UpLeft to Anims33.ShootFromUpLeft,
            GDir.Up to Anims33.ShootFromUp,
            GDir.UpRight to Anims33.ShootFromUpRight
    )
}