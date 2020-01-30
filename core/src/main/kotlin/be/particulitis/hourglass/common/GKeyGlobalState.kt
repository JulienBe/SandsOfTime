package be.particulitis.hourglass.common

import com.badlogic.gdx.Gdx

object GKeyGlobalState {

    var touched = false
    var justReleased = false
    var justTouched = false

    fun act() {
        justReleased = false
        if (touched && !Gdx.input.isTouched)
            justReleased = true
        justTouched = Gdx.input.justTouched()
        touched = Gdx.input.isTouched

    }
}
