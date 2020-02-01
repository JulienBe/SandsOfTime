package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GPeriodicValue
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.Phases
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.system.SysCollider
import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.math.Vector2

object SBullet : Setup() {

    fun enemyBullet(world: World, posX: Float, posY: Float, dir: Vector2, str: Int): Entity {
        val bullet = world.create(Builder.bullet)
        bullet.space().setDim(Dim.Bullet)
        bullet.space().setPos(posX, posY)
        dir.scl(22f)
        val space = bullet.space()
        val draw = bullet.draw()

        bullet.dir().set(dir)
        bullet.dir().setSpeedAcceleration(100f, 100f)
        bullet.layer().setLayer(Phases.Enemy)
        bullet.hp().setHp(1)
        bullet.collide().setIds(Ids.enemyBullet)
        bullet.collide().addCollidingWith(Ids.player)
        bullet.ttl().remaining = 9f
        bullet.light().setLight(Colors.enemyBullets, posX, posY, 0.04f)
        draw.color = Colors.enemyBullets
//        draw.drawFront = { DrawMethods.basic(space, draw, it)}
        draw.layer = Data.enemyBulletLayer
        return bullet
    }

    fun playerBullet(world: World, posX: Float, posY: Float, desiredDir: Vector2, str: Int): Entity {
        val bullet = world.create(Builder.bullet)
        bullet.space().setDim(Dim.Bullet)
        bullet.space().setPos(posX, posY)
        desiredDir.scl(160f)
        val space = bullet.space()
        val draw = bullet.draw()
        val dirComp = bullet.dir()
        dirComp.set(desiredDir)
        bullet.layer().setLayer(Phases.Player)
        bullet.hp().setHp(100000)

        bullet.collide().setIds(Ids.playerBullet)
        bullet.collide().addCollidingWith(Ids.enemy, Ids.propsWall, Ids.player)
        bullet.collide().collidingMap.put(Ids.propsWall.id, world.getSystem(SysCollider::class.java)::bounceOfWall)

        bullet.ttl().remaining = 2f
        val light = bullet.light()
        light.setLight(Colors.playerBullets, posX, posY, 0.06f)
        val intensityRandomness = GPeriodicValue(0.08f) {
            GRand.nextGaussian().toFloat() / 1000f
        }
        draw.color = Colors.playerBullets
        draw.currentImg = GGraphics.img("square_yellow")
        draw.preDraw = {
            light.updatePos(space.centerX, space.centerY)
            intensityRandomness.tick(GTime.delta)
            light.updateIntesity((0.1f + intensityRandomness.value) * (1 + (str / 5)) )
            for (i in 0..4 * str)
                SParticles.fireParticle(world, space.centerX, space.centerY, 1.5f)
        }
        draw.layer = Data.playerBulletLayer
        return bullet
    }

}