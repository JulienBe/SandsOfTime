package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.comp.ui.CompButton
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.comp.ui.CompTxt

enum class Aspects(val comps: List<Class<out Comp>>) {

    EnemySlug(listOf(
            CompTimePhase::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompAction::class.java,
            CompHp::class.java,
            CompEnemy::class.java,
            CompDir::class.java,
            CompTargetSeek::class.java,
            CompTargetFollow::class.java,
            CompParticleEmitter::class.java)),
    EnemyShoot(listOf(
            CompTimePhase::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompAction::class.java,
            CompHp::class.java,
            CompEnemy::class.java,
            CompShooter::class.java,
            CompParticleEmitter::class.java)),
    Player(listOf(
            CompTimePhase::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompControl::class.java,
            CompCharMovement::class.java,
            CompAction::class.java,
            CompHp::class.java,
            CompShooter::class.java,
            CompLights::class.java)),
    Bullet(listOf(
            CompTimePhase::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompHp::class.java,
            CompDir::class.java,
            CompTtl::class.java,
            CompLights::class.java)),
    Score(listOf(
            CompSpace::class.java,
            CompTxt::class.java,
            CompScore::class.java
    )),
    PrettyDisplay(listOf(
            CompSpace::class.java,
            CompPrettyUi::class.java
    )),
    Button(listOf(
            CompSpace::class.java,
            CompButton::class.java
    )),
    Dent(listOf(
            CompSpace::class.java,
            CompDraw::class.java
    )),
    Tiled(listOf(
            CompSpace::class.java,
            CompDraw::class.java
    )),
    Wall(listOf(
            CompSpace::class.java,
            CompCollide::class.java,
            CompDraw::class.java,
            CompSide::class.java
    )),
    OccluderProp(listOf(
            CompSpace::class.java,
            CompDraw::class.java
    )),
    BloodParticles(listOf(
            CompDraw::class.java,
            CompSpace::class.java,
            CompParticle::class.java,
            CompTimePhase::class.java,
            CompTtl::class.java,
            CompDir::class.java
    ))
}