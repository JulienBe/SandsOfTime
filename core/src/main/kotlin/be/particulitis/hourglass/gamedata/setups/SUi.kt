package be.particulitis.hourglass.gamedata.setups

import com.artemis.World

object SUi : Setup() {
    fun score(id: Int, world: World) {
        world.getEntity(id).space().setPos(90f, 170f)
    }
    fun prettyDisplay(id: Int, world: World, text: String, x: Float, y: Float) {
        val space = world.getEntity(id).space()
        val ui = world.getEntity(id).prettyUi()
        space.setPos(x, y)
        ui.changeText(text, 1, 1f)
    }
    fun button(id: Int, world: World, text: String, x: Float, y: Float, onClick: () -> Unit) {
        val space = world.getEntity(id).space()
        val ui = world.getEntity(id).button()
        space.setPos(x, y)
        ui.set(text)
        ui.onClick = onClick
    }
}