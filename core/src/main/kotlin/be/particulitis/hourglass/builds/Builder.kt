package be.particulitis.hourglass.builds

import be.particulitis.hourglass.comp.*
import com.artemis.ArchetypeBuilder

object Builder {
    val player = ArchetypeBuilder()
            .add(CompSpace::class.java)
            .add(CompDraw::class.java)
            .add(CompCollide::class.java)
            .add(CompControl::class.java)
            .add(CompCharMovement::class.java)
            .add(CompAction::class.java)
            .add(CompHp::class.java)
            .add(CompShooter::class.java)
    val enemy = ArchetypeBuilder()
            .add(CompSpace::class.java)
            .add(CompDraw::class.java)
            .add(CompCollide::class.java)
            .add(CompAction::class.java)
            .add(CompHp::class.java)
            .add(CompEnemy::class.java)
    val bullet = ArchetypeBuilder()
            .add(CompSpace::class.java)
            .add(CompDraw::class.java)
            .add(CompCollide::class.java)
            .add(CompHp::class.java)
            .add(CompDir::class.java)
            .add(CompTtl::class.java)
}
