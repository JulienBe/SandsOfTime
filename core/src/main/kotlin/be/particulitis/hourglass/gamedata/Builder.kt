package be.particulitis.hourglass.gamedata

import com.artemis.ArchetypeBuilder

object Builder {
    val player = createBuilder(Aspects.Player)
    val bullet = createBuilder(Aspects.Bullet)
    val enemySlug = createBuilder(Aspects.EnemySlug)
    val enemyShoot = createBuilder(Aspects.EnemyShoot)
    val score = createBuilder(Aspects.Score)
    val explosionParticle = createBuilder(Aspects.ExplosionParticle)
    val prettyDisplay = createBuilder(Aspects.PrettyDisplay)
    val button = createBuilder(Aspects.Button)
    val wall = createBuilder(Aspects.Wall)
    val dent = createBuilder(Aspects.Dent)

    private fun createBuilder(aspects: Aspects): ArchetypeBuilder {
        val builder = ArchetypeBuilder()
        aspects.comps.forEach { builder.add(it) }
        return builder
    }
}
