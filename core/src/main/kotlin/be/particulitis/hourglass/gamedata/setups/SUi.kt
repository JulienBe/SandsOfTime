package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.font.FlipColorAnim
import be.particulitis.hourglass.font.FontPixel
import be.particulitis.hourglass.gamedata.Builder
import com.artemis.World

object SUi : Setup() {

    val buttonPadding = 2

    fun score(id: Int, world: World) {
        world.getEntity(id).space().setPos(90f, 170f)
    }
    fun prettyDisplay(world: World, text: String, x: Float, y: Float, w: Int = 1) {
        val e = world.create(Builder.prettyDisplay)
        val space = e.space()
        space.setPos(x, y)
        e.prettyUi().changeText(text, w)
        e.prettyUi().phases.add(
                FlipColorAnim()
        )
    }

    fun button(world: World, text: String, x: Float, y: Float, w: Int = 1, onClick: () -> Unit) {
        val e = world.create(Builder.button)
        val space = e.space()
        val ui = e.button()
        space.setPos(x, y)
        ui.set(text, w)
        ui.onClick = onClick
        var delay = 0f
        for (i in -buttonPadding..ui.w) {
            ui.outline.add(FontPixel(i.toFloat(), ui.h.toFloat(), true, delay))
            delay += 0.025f
        }
        for (i in (ui.h - 1) downTo -(buttonPadding + 1)) {
            ui.outline.add(FontPixel(ui.w.toFloat(), i.toFloat(), true, delay))
            delay += 0.025f
        }
        for (i in ui.w downTo -(buttonPadding)) {
            ui.outline.add(FontPixel(i.toFloat(), (-buttonPadding - 2).toFloat(), true, delay))
            delay += 0.025f
        }
        for (i in -(buttonPadding + 1)..ui.h) {
            ui.outline.add(FontPixel(-buttonPadding.toFloat(), i.toFloat(), true, delay))
            delay += 0.025f
        }

    }
}