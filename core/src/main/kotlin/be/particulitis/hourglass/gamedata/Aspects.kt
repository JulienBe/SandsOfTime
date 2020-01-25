package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.comp.ui.CompButton
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.comp.ui.CompTxt

enum class Aspects(val comps: List<Class<out Comp>>) {

    EnemySlug(listOf(
            CompLayer::class.java,
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
            CompLayer::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompAction::class.java,
            CompHp::class.java,
            CompEnemy::class.java,
            CompShooter::class.java,
            CompParticleEmitter::class.java)),
    Player(listOf(
            CompLayer::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompControl::class.java,
            CompCharMovement::class.java,
            CompAction::class.java,
            CompHp::class.java,
            CompShooter::class.java,
            CompLight::class.java,
            CompOccluder::class.java)),
    Bullet(listOf(
            CompLayer::class.java,
            CompSpace::class.java,
            CompDraw::class.java,
            CompCollide::class.java,
            CompHp::class.java,
            CompDir::class.java,
            CompTtl::class.java,
            CompLight::class.java)),
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
    Wall(listOf(
            CompSpace::class.java,
            CompDraw::class.java
    )),
    OccluderProp(listOf(
            CompOccluder::class.java,
            CompSpace::class.java,
            CompDraw::class.java
    )),
    ExplosionParticle(listOf(
            CompDraw::class.java,
            CompSpace::class.java,
            CompParticle::class.java,
            CompLayer::class.java,
            CompTtl::class.java,
            CompDir::class.java,
            CompLight::class.java
    ))
}