package be.particulitis.hourglass.builds

import be.particulitis.hourglass.comp.*

enum class Aspects(val comps: List<Class<out Comp>>) {

    EnemySlug(listOf(
            CompIsPlayer::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompAction::class.java,
            CompHp::class.java,
            CompEnemy::class.java,
            CompDir::class.java,
            CompTargetSeek::class.java,
            CompTargetFollow::class.java)),
    EnemyShoot(listOf(
            CompIsPlayer::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompAction::class.java,
            CompHp::class.java,
            CompEnemy::class.java,
            CompShooter::class.java)),
    Player(listOf(
            CompIsPlayer::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompControl::class.java,
            CompCharMovement::class.java,
            CompAction::class.java,
            CompHp::class.java,
            CompShooter::class.java)),
    Bullet(listOf(
            CompIsPlayer::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompHp::class.java,
            CompDir::class.java,
            CompTtl::class.java)),
    Score(listOf(
            CompSpace::class.java,
            CompTxt::class.java,
            CompScore::class.java
    ))
}