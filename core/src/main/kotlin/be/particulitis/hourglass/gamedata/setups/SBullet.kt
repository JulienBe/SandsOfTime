package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.Layers
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.World
import com.badlogic.gdx.math.Vector2

object SBullet {

    fun enemyBullet(id: Int, world: World, posX: Float, posY: Float, dir: Vector2) {
        val dim = world.getEntity(id).getComponent(CompSpace::class.java)
        dim.setDim(Dim.Bullet)
        dim.setPos(posX, posY)
        dir.scl(22f)
        val bullet = world.getEntity(id)
        val space = bullet.getComponent(CompSpace::class.java)
        val draw = bullet.getComponent(CompDraw::class.java)
        val collide = bullet.getComponent(CompCollide::class.java)
        val light = bullet.getComponent(CompLight::class.java)

        bullet.getComponent(CompDir::class.java).set(dir)
        bullet.getComponent(CompDir::class.java).setSpeedAcceleration(100f, 100f)
        bullet.getComponent(CompLayer::class.java).setLayer(Layers.Enemy)
        bullet.getComponent(CompHp::class.java).setHp(1)
        collide.setIds(Ids.enemyBullet)
        collide.addCollidingWith(Ids.player)
        bullet.getComponent(CompTtl::class.java).remaining = 9f
        light.setLight(Colors.enemyBullets, posX, posY, 0.1f)
        draw.color = Colors.enemyBullets
        draw.drawingStyle = {batch -> DrawMethods.basic(space, draw, batch)}
        draw.layer = Data.enemyBulletLayer
    }

    fun playerBullet(id: Int, world: World, posX: Float, posY: Float, dir: Vector2) {
        val dim = world.getEntity(id).getComponent(CompSpace::class.java)
        dim.setDim(Dim.Bullet)
        dim.setPos(posX, posY)
        dir.scl(160f)
        val bullet = world.getEntity(id)
        val collide = bullet.getComponent(CompCollide::class.java)
        val space = bullet.getComponent(CompSpace::class.java)
        val draw = bullet.getComponent(CompDraw::class.java)
        val light = bullet.getComponent(CompLight::class.java)
        bullet.getComponent(CompDir::class.java).set(dir)
        bullet.getComponent(CompLayer::class.java).setLayer(Layers.Player)
        bullet.getComponent(CompHp::class.java).setHp(100000)
        collide.setIds(Ids.playerBullet)
        collide.addCollidingWith(Ids.enemy)
        bullet.getComponent(CompTtl::class.java).remaining = 1f
        light.setLight(Colors.playerBullets, posX, posY, 0.2f)
        draw.color = Colors.playerBullets
        draw.drawingStyle = {batch -> DrawMethods.basic(space, draw, batch)}
        draw.layer = Data.playerBulletLayer
    }

}