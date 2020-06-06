package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.*
import be.particulitis.hourglass.common.GPeriodicValue
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.Phases
import be.particulitis.hourglass.system.SysCollider
import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2

object SBullet {

    fun gunnerBullet(world: World, posX: Float, posY: Float, angle: Float): Entity {
        val bullet = world.create(Builder.bullet)
        val space = bullet.space()
        val dir = bullet.dir()
        val hp = bullet.hp()
        val collide = bullet.collide()
        val layer = bullet.layer()
        val ttl = bullet.ttl()
        val act = bullet.act()
        val light = GLight(posX, posY, 0.05f, GPalette.RED)

        val onEnd = {
            light.clear()
        }

        space.setDim(Dim.Bullet)
        space.setPos(posX, posY)

        dir.set(20f, 0f)
        dir.setAngle(angle)

        layer.setLayer(Phases.Enemy)

        hp.setHp(2)
        hp.onDead = onEnd

        ttl.remaining = 6f
        ttl.onEnd = onEnd

        collide.setDmgToInflict(5)
        collide.setIds(Ids.enemyBullet)

        act.act = {
            light.updatePos(space.centerX, space.centerY)
            if (Gdx.graphics.frameId % 60L == 0L)
            for (i in 0..0)
                SParticles.fireParticle(world, space.centerX, space.centerY, 1f)
        }

        return bullet
    }

    private var globalPink = true
    fun playerBullet(world: World, posX: Float, posY: Float, desiredDir: Vector2, str: Int): Entity {
        val bullet = world.create(Builder.bullet)
        val space = bullet.space()
        val dirComp = bullet.dir()
        val layer = bullet.layer()
        val hp = bullet.hp()
        val collide = bullet.collide()
        val ttl = bullet.ttl()

        space.setDim(Dim.Bullet)
        space.setPos(posX, posY)
        desiredDir.scl(160f)
        dirComp.set(desiredDir)
        layer.setLayer(Phases.Player)
        hp.setHp(str)

        collide.setDmgToInflict(5)
        collide.setIds(Ids.playerBullet)
        collide.addCollidingWith(Ids.enemy, Ids.propsWall, Ids.player)
        collide.collidingMap.put(Ids.propsWall.id, world.getSystem(SysCollider::class.java)::bounceOfWall)

        ttl.remaining = 2f

        globalPink = !globalPink
        val pink = globalPink
        val palette = if (pink) GPalette.PINK_SKIN else GPalette.LAVENDER
        val light = GLight(posX, posY, 0.06f, 0f, 0f, palette.r, palette.g, palette.b)
        val intensityRandomness = GPeriodicValue(0.08f) {
            GRand.nextGaussian().toFloat() / 1000f
        }
        bullet.act().act = {
            light.updatePos(space.centerX, space.centerY)
            intensityRandomness.tick(GTime.delta)
            light.updateIntesityRGB((0.1f + intensityRandomness.value) * (1 + (str / 5)), palette)
            for (i in 0..20)
                SParticles.lollipopShoot(world, space.centerX, space.centerY, pink)
            SParticles.lollipopTrail(world, space.centerX, space.centerY, (GTime.time * 12f), pink)
            SParticles.lollipopTrail(world, space.centerX, space.centerY, (GTime.time * -12f), pink)
            dirComp.v.setLength(160f)
        }
        val onEnd = {
            light.clear()
        }
        hp.onDead = onEnd
        ttl.onEnd = onEnd
        return bullet
    }

}