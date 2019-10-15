package be.particulitis.hourglass.common

import com.badlogic.gdx.Gdx

object GSounds {
    public val explosion1 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion1.wav"))
    public val explosion2 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion2.wav"))
    public val explosion3 = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion3.wav"))
    public val swaptime = Gdx.audio.newSound(Gdx.files.internal("sounds/swaptime.wav"))
    public val shot = Gdx.audio.newSound(Gdx.files.internal("sounds/shot.wav"))
}