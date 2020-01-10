package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.gamedata.*
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.TagManager

object SEnemy : Setup() {

    fun enemyShoot(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float) {
        val enemy = baseEnemy(id, world, exclusionStartX, exclusionStopX, exclusionStartY, exclusionStopY)
        val space = enemy.space()
        val shoot = enemy.shooter()
        val draw = enemy.draw()

        draw.color = Colors.enemyShoots
        draw.drawingStyle = {batch, tr ->
            DrawMethods.basic(space, draw, batch)
            //DrawMethods.draw33animLoop(space, draw, Anims33.SquareNoDir, 2, Dim.Enemy, batch)
            draw.cpt = (GTime.enemyTime * 10f).toInt()
        }

        shoot.setOffset(Dim.Enemy.half - Dim.Bullet.half, Dim.Enemy.half - Dim.Bullet.half)
        shoot.shouldShood = { true }
        shoot.setBullet(Builder.bullet, SBullet::enemyBullet)
        shoot.shootingFunc = {
            val playerSpace = world.getSystem(TagManager::class.java).getEntity(Data.playerTag).space()
            shoot.dir.set(playerSpace.centerX - (space.x + Dim.Enemy.half), playerSpace.centerY - (space.y + Dim.Enemy.half))
            shoot.dir.nor()
            shoot.bullet.second.invoke(world,
                    space.x + shoot.offsetX + shoot.dir.x / 100f, space.y + shoot.offsetY + shoot.dir.y / 100f,
                    shoot.dir)
        }
        shoot.setFirerate(2f)

        enemy.emitter().emit = {
            for (i in 0..20)
                SParticles.explosionParticle(world, space.centerX, space.centerY, 18f)
        }
    }

    fun enemySlug(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float) {
        val enemy = baseEnemy(id, world, exclusionStartX, exclusionStopX, exclusionStartY, exclusionStopY)
        val player = world.getSystem(TagManager::class.java).getEntity(Data.playerTag)
        val space = enemy.space()
        val draw = enemy.draw()
        val dir = enemy.dir()

        enemy.targetSeek().target.set(GRand.nextFloat() * 100f, GRand.nextFloat() * 100f)
        enemy.targetFollow().set(player.space())
        enemy.dir().setSpeedAcceleration(20f, 0.3f)
        draw.drawingStyle = { batch, tr ->
            DrawMethods.basic(space, draw, batch)
            DrawMethods.drawTrail(draw, space, dir, batch)
        }

        enemy.emitter().emit = {
            for (i in 0..40)
                SParticles.explosionParticle(world, space.centerX, space.centerY, 28f)
        }
    }

    private fun baseEnemy(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float): Entity {
        val enemy = world.getEntity(id)
        enemy.space().setDim(Dim.Enemy.w, Dim.Enemy.w)
        enemy.space().setPos(GRand.floatExcludingPlease(0f, GResolution.areaDim - Dim.Enemy.w, exclusionStartX, exclusionStopX), GRand.floatExcludingPlease(0f, GResolution.areaDim - Dim.Enemy.w, exclusionStartY, exclusionStopY))
        enemy.collide().setIds(Ids.enemy)
        enemy.collide().addCollidingWith(Ids.player, Ids.playerBullet)
        enemy.layer().setLayer(Layers.Enemy)
        enemy.draw().color = Colors.enemy
        enemy.draw().layer = Data.enemyLayer
        return enemy
    }


}