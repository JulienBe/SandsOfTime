package be.particulitis.hourglass.common

import com.badlogic.gdx.Gdx

object GSounds {
    val explosion1 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.wav"))
    val explosion2 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion2.wav"))
    val explosion3 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion3.wav"))
    val swaptime = Gdx.audio.newSound(Gdx.files.internal("sounds/swaptime.wav"))
    val shot = Gdx.audio.newSound(Gdx.files.internal("sounds/shot.wav"))
    val pixelSnap = Gdx.audio.newSound(Gdx.files.internal("sounds/pixelsnap.wav"))
}