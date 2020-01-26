package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.GPeriodicValue
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.Layers
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.World
import com.badlogic.gdx.math.Vector2

object SBullet : Setup() {

    fun enemyBullet(world: World, posX: Float, posY: Float, dir: Vector2) {
        val bullet = world.create(Builder.bullet)
        bullet.space().setDim(Dim.Bullet)
        bullet.space().setPos(posX, posY)
        dir.scl(22f)
        val space = bullet.space()
        val draw = bullet.draw()

        bullet.dir().set(dir)
        bullet.dir().setSpeedAcceleration(100f, 100f)
        bullet.layer().setLayer(Layers.Enemy)
        bullet.hp().setHp(1)
        bullet.collide().setIds(Ids.enemyBullet)
        bullet.collide().addCollidingWith(Ids.player)
        bullet.ttl().remaining = 9f
        bullet.light().setLight(Colors.enemyBullets, posX, posY, 0.04f)
        draw.color = Colors.enemyBullets
        draw.drawFront = { DrawMethods.basic(space, draw, it)}
        draw.layer = Data.enemyBulletLayer
    }

    fun playerBullet(world: World, posX: Float, posY: Float, dir: Vector2) {
        val bullet = world.create(Builder.bullet)
        bullet.space().setDim(Dim.Bullet)
        bullet.space().setPos(posX, posY)
        dir.scl(160f)
        val space = bullet.space()
        val draw = bullet.draw()
        bullet.dir().set(dir)
        bullet.layer().setLayer(Layers.Player)
        bullet.hp().setHp(100000)
        bullet.collide().setIds(Ids.playerBullet)
        bullet.collide().addCollidingWith(Ids.enemy)
        bullet.ttl().remaining = 1f
        val light = bullet.light()
        light.setLight(Colors.playerBullets, posX, posY, 0.06f)
        val intensityRandomness = GPeriodicValue(0.08f) {
            GRand.nextGaussian().toFloat() / 10f
        }
        draw.color = Colors.playerBullets
        draw.drawFront = {
            light.updatePos(space.centerX, space.centerY)
            light.updateIntesity(0.06f + intensityRandomness.value)
            for (i in 0..10)
                SParticles.fireParticle(world, space.centerX, space.centerY, 1f)
        }
        draw.layer = Data.playerBulletLayer
    }

}