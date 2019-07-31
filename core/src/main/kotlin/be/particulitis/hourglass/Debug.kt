package be.particulitis.hourglass

import com.artemis.Component
import com.artemis.World
import com.artemis.utils.Bag
import javax.swing.text.html.parser.Entity

object Debug {

    fun entity(id: Int, world: World) {
        val e = world.getEntity(id)
        println("\tentity: $id")
        println("\tactive: ${e.isActive}")
        println("\tcompositionId: ${e.compositionId}")
        val bag = Bag<Component>()
        e.getComponents(bag)
        println("\tnb components: ${bag.size()}")
        bag.forEach {
            println("\tComponent: $it")
        }
    }

}