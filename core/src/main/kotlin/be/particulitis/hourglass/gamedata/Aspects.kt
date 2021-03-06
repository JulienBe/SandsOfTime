package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.comp.draw.CompBloomer
import be.particulitis.hourglass.comp.draw.CompDraw
import be.particulitis.hourglass.comp.draw.CompUndertrail
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
            CompShooter::class.java
    )),
    Bullet(listOf(
            CompTimePhase::class.java,
            CompSpace::class.java,
            CompCollide::class.java,
            CompHp::class.java,
            CompDir::class.java,
            CompTtl::class.java,
            CompAct::class.java
    )),
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
    BloomParticles(listOf(
            CompBloomer::class.java,
            CompSpace::class.java,
            CompParticle::class.java,
            CompTimePhase::class.java,
            CompTtl::class.java,
            CompDir::class.java
    )),
    TrailParticlesBloomer(listOf(
            CompBloomer::class.java,
            CompSpace::class.java,
            CompParticle::class.java,
            CompTimePhase::class.java,
            CompTtl::class.java
    )),
    TrailParticles(listOf(
            CompDraw::class.java,
            CompSpace::class.java,
            CompParticle::class.java,
            CompTimePhase::class.java,
            CompTtl::class.java
    )),
    Particles(listOf(
            CompDraw::class.java,
            CompSpace::class.java,
            CompParticle::class.java,
            CompTimePhase::class.java,
            CompTtl::class.java,
            CompDir::class.java
    )),
    Beacon(listOf(
            CompTimePhase::class.java,
            CompSpace::class.java,
            CompTtl::class.java
    )),
    DotUndertrail(listOf(
            CompUndertrail::class.java,
            CompSpace::class.java
    ))
}
