package be.particulitis.hourglass.archetypes

import be.particulitis.hourglass.comp.*
import com.artemis.ArchetypeBuilder

object Builder {
    val player = ArchetypeBuilder()
            .add(CompDimension::class.java)
            .add(CompDraw::class.java)
            .add(CompCollide::class.java)
            .add(CompControl::class.java)
            .add(CompCharMovement::class.java)
            .add(CompAction::class.java)
}