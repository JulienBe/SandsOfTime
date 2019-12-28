package be.particulitis.hourglass.screens

import be.particulitis.hourglass.common.GGraphics
import be.particulitis.hourglass.font.FontPrettyDisplay
import com.badlogic.gdx.ScreenAdapter

class TitleScreen : AbstractScreen() {

    val anim = FontPrettyDisplay("Hourglass", 180f, 200f)

    override fun render(delta: Float) {
        GGraphics.render {
            super.render(delta)
            anim.draw(GGraphics.batch)
        }
    }
}