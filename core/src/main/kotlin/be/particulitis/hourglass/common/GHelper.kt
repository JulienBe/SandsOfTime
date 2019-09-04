package be.particulitis.hourglass.common

import com.badlogic.gdx.Gdx

object GHelper {
    val percentX get() = ((Gdx.input.x.toFloat() / Gdx.graphics.width) -
            // offset by screen unused
            (1 - GResolution.percentageUsedX) / 2f) /
            // scale to screen used
            GResolution.percentageUsedX
    val percentY get(): Float = 1 - (
                // screen percentage
            (((Gdx.input.y).toFloat() / Gdx.graphics.height) -
                // offset by screen unused
            (1 - GResolution.percentageUsedY) / 2f) /
                // scale to screen used
            GResolution.percentageUsedY)
    val x get() = percentX * GResolution.areaDim
    val y get() = percentY * GResolution.areaDim

}