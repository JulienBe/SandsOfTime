package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.gamedata.*
import be.particulitis.hourglass.gamedata.graphics.Anims33
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.TagManager

object SEnemy {

    fun enemyShoot(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float) {
        val enemy = baseEnemy(id, world, exclusionStartX, exclusionStopX, exclusionStartY, exclusionStopY)
        val space = enemy.getComponent(CompSpace::class.java)
        val shoot = enemy.getComponent(CompShooter::class.java)
        val draw = enemy.getComponent(CompDraw::class.java)
        val particleEmitter = enemy.getComponent(CompParticleEmitter::class.java)

        draw.color = Colors.enemyShoots
        draw.drawingStyle = {batch ->
            DrawMethods.draw33animLoop(space, draw, Anims33.SquareNoDir, 2, Dim.Enemy, batch)
            draw.cpt = (GTime.enemyTime * 10f).toInt()
        }

        shoot.setOffset(Dim.Enemy.half - Dim.Bullet.half, Dim.Enemy.half - Dim.Bullet.half)
        shoot.shouldShood = { true }
        shoot.setBullet(Builder.bullet, SBullet::enemyBullet)
        shoot.shootingFunc = {
            val bulletId = world.create(shoot.bullet.first.build(world))
            val playerSpace = world.getSystem(TagManager::class.java).getEntity(Data.playerTag).getComponent(CompSpace::class.java)
            shoot.dir.set(playerSpace.centerX - (space.x + Dim.Enemy.half), playerSpace.centerY - (space.y + Dim.Enemy.half))
            shoot.dir.nor()
            shoot.bullet.second.invoke(bulletId, world,
                    space.x + shoot.offsetX + shoot.dir.x / 100f, space.y + shoot.offsetY + shoot.dir.y / 100f,
                    shoot.dir)
        }
        shoot.setFirerate(2f)

        particleEmitter.emit = {
            for (i in 0..20)
                SParticles.explosionParticle(world.create(Builder.explosionParticle.build(world)), world, space.centerX, space.centerY, 18f)
        }
    }

    fun enemySlug(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float) {
        val enemy = baseEnemy(id, world, exclusionStartX, exclusionStopX, exclusionStartY, exclusionStopY)
        val player = world.getSystem(TagManager::class.java).getEntity(Data.playerTag)
        val space = enemy.getComponent(CompSpace::class.java)
        val draw = enemy.getComponent(CompDraw::class.java)
        val dir = enemy.getComponent(CompDir::class.java)
        val particleEmitter = enemy.getComponent(CompParticleEmitter::class.java)

        enemy.getComponent(CompTargetSeek::class.java).target.set(GRand.nextFloat() * 100f, GRand.nextFloat() * 100f)
        enemy.getComponent(CompTargetFollow::class.java).set(player.getComponent(CompSpace::class.java))
        enemy.getComponent(CompDir::class.java).setSpeedAcceleration(20f, 0.3f)
        draw.drawingStyle = { batch ->
            DrawMethods.basic(space, draw, batch)
            DrawMethods.drawTrail(draw, space, dir, batch)
        }

        particleEmitter.emit = {
            for (i in 0..40)
                SParticles.explosionParticle(world.create(Builder.explosionParticle.build(world)), world, space.centerX, space.centerY, 28f)
        }
    }

    private fun baseEnemy(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float): Entity {
        val dim = world.getEntity(id).getComponent(CompSpace::class.java)
        dim.setDim(Dim.Enemy.w, Dim.Enemy.w)
        dim.setPos(GRand.floatExcludingPlease(0f, GResolution.areaDim - Dim.Enemy.w, exclusionStartX, exclusionStopX), GRand.floatExcludingPlease(0f, GResolution.areaDim - Dim.Enemy.w, exclusionStartY, exclusionStopY))
        val enemy = world.getEntity(id)
        val collide = enemy.getComponent(CompCollide::class.java)
        collide.setIds(Ids.enemy)
        collide.addCollidingWith(Ids.player, Ids.playerBullet)
        enemy.getComponent(CompLayer::class.java).setLayer(Layers.Enemy)
        enemy.getComponent(CompDraw::class.java).color = Colors.enemy
        enemy.getComponent(CompDraw::class.java).layer = Data.enemyLayer
        return enemy
    }


}