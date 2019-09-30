package be.particulitis.hourglass.builds

import be.particulitis.hourglass.comp.*
import com.artemis.ArchetypeBuilder

object Builder {
    val player = createBuilder(Aspects.Player)
    val bullet = createBuilder(Aspects.Bullet)
    val enemy= createBuilder(Aspects.Enemy)
    val score = createBuilder(Aspects.Score)

    private fun createBuilder(aspects: Aspects): ArchetypeBuilder {
        val builder = ArchetypeBuilder()
        aspects.comps.forEach { builder.add(it) }
        return builder
    }
}
