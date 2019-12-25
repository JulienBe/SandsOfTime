package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.comp.CompSpace
import com.artemis.World

object SUi {
    fun score(id: Int, world: World) {
        val space = world.getEntity(id).getComponent(CompSpace::class.java)
        space.setPos(90f, 170f)
    }
}