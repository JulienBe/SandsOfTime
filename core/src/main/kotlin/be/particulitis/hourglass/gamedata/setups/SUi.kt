package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.ui.CompButton
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import com.artemis.World

object SUi {
    fun score(id: Int, world: World) {
        val space = world.getEntity(id).getComponent(CompSpace::class.java)
        space.setPos(90f, 170f)
    }
    fun prettyDisplay(id: Int, world: World, text: String, x: Float, y: Float) {
        val space = world.getEntity(id).getComponent(CompSpace::class.java)
        val ui = world.getEntity(id).getComponent(CompPrettyUi::class.java)
        space.setPos(x, y)
        ui.setText(text, 1)
    }
    fun button(id: Int, world: World, text: String, x: Float, y: Float, onClick: () -> Unit) {
        val space = world.getEntity(id).getComponent(CompSpace::class.java)
        val ui = world.getEntity(id).getComponent(CompButton::class.java)
        space.setPos(x, y)
        ui.set(text)
        ui.onClick = onClick
    }
}