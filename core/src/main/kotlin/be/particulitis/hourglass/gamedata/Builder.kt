package be.particulitis.hourglass.gamedata

import com.artemis.ArchetypeBuilder

object Builder {
    val player = createBuilder(Aspects.Player)
    val bullet = createBuilder(Aspects.Bullet)
    val enemyCpu = createBuilder(Aspects.EnemySlug)
    val enemyShoot = createBuilder(Aspects.EnemyShoot)
    val score = createBuilder(Aspects.Score)
    val bloodParticle = createBuilder(Aspects.BloomParticles)
    val trailParticleBloomer = createBuilder(Aspects.TrailParticlesBloomer)
    val trailParticle = createBuilder(Aspects.TrailParticles)
    val particle = createBuilder(Aspects.Particles)
    val prettyDisplay = createBuilder(Aspects.PrettyDisplay)
    val button = createBuilder(Aspects.Button)
    val tiled = createBuilder(Aspects.Tiled)
    val wall = createBuilder(Aspects.Wall)
    val dent = createBuilder(Aspects.Dent)
    val occluderProp = createBuilder(Aspects.OccluderProp)
    val beacon = createBuilder(Aspects.Beacon)

    private fun createBuilder(aspects: Aspects): ArchetypeBuilder {
        val builder = ArchetypeBuilder()
        aspects.comps.forEach { builder.add(it) }
        return builder
    }
}
