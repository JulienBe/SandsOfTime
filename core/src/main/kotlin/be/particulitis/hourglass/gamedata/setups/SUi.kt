package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.gamedata.Builder
import com.artemis.World

object SUi : Setup() {
    fun score(id: Int, world: World) {
        world.getEntity(id).space().setPos(90f, 170f)
    }
    fun prettyDisplay(world: World, text: String, x: Float, y: Float) {
        val e = world.create(Builder.prettyDisplay)
        val space = e.space()
        space.setPos(x, y)
        e.prettyUi().changeText(text, 1, 1f)
    }

    fun button(world: World, text: String, x: Float, y: Float, onClick: () -> Unit) {
        val e = world.create(Builder.button)
        val space = e.space()
        val ui = e.button()
        space.setPos(x, y)
        ui.set(text)
        ui.onClick = onClick
    }
}