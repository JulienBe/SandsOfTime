package be.particulitis.hourglass.builds

import be.particulitis.hourglass.comp.*
import com.artemis.ArchetypeBuilder

object Builder {
    val player = ArchetypeBuilder()
            .add(CompIsPlayer::class.java)
            .add(CompSpace::class.java)
            .add(CompDraw::class.java)
            .add(CompCollide::class.java)
            .add(CompControl::class.java)
            .add(CompCharMovement::class.java)
            .add(CompAction::class.java)
            .add(CompHp::class.java)
            .add(CompShooter::class.java)
    val enemy = ArchetypeBuilder()
            .add(CompIsPlayer::class.java)
            .add(CompSpace::class.java)
            .add(CompDraw::class.java)
            .add(CompCollide::class.java)
            .add(CompAction::class.java)
            .add(CompHp::class.java)
            .add(CompEnemy::class.java)
            .add(CompDir::class.java)
            .add(CompTargetSeek::class.java)
            .add(CompTargetFollow::class.java)
    val bullet = ArchetypeBuilder()
            .add(CompIsPlayer::class.java)
            .add(CompSpace::class.java)
            .add(CompDraw::class.java)
            .add(CompCollide::class.java)
            .add(CompHp::class.java)
            .add(CompDir::class.java)
            .add(CompTtl::class.java)
}
