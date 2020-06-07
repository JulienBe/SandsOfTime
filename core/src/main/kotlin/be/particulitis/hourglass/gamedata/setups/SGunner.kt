package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.*
import be.particulitis.hourglass.common.GSounds
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.*
import be.particulitis.hourglass.common.puppet.GAnim
import be.particulitis.hourglass.common.puppet.GAnimController
import be.particulitis.hourglass.comp.CompCollide
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.draw.CompDraw
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.graphics.Frames
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs
import kotlin.math.roundToInt

object SGunner {

    private val tempVector = Vector2()
    private const val baseLightIntensity = 0.00f
    private const val highLightIntensity = 0.075f

    fun setup(world: World, baseX: Float, baseY: Float) {
        val enemy = world.create(Builder.enemyShoot)
        val player = world.getSystem(TagManager::class.java).getEntity(Data.playerTag)
        val playerSpace = player.space()
        val space = enemy.space()
        val draw = enemy.draw()
        val dir = Vector2(5f, 0f)
        val collider = enemy.collide()
        val hp = enemy.hp()
        val light = GLight(space.centerX, space.centerY, baseLightIntensity)
        val onDeadEmitter = enemy.emitter()

        val spawnParticleTtl = SParticles.gunnerSpawn(world, space, dir.angle() + 90f)
        hp.setHp(15)
        hp.onDead = {
            light.clear()
            spawnParticleTtl.remaining = -1f
        }

        setupCollider(collider, true)


        space.setDim(Dim.Enemy.w, Dim.Enemy.w)
        space.setPos(baseX, baseY)
        draw.layer = Data.enemyLayer
        draw.currentImg = GGraphics.red
        val shoot = GAnim(Frames.GUNNER_SHOOT)
        val spawn = GAnim(Frames.GUNNER_SPAWN)
        val rotate = GAnim(Frames.GUNNER_ROTATE, timePerFrame = 0.06f)
        val animController = GAnimController(spawn)

        shoot.onFrame[1] = {
            blinkLight(world, draw, dir, light, space)
        }
        shoot.onFrame[2] = {
            light.updateIntesity(highLightIntensity * 0.25f)
        }
        shoot.lastOnFunction {
            SBullet.gunnerBullet(world, space.centerX - (dir.x * 5f), space.y - (dir.y * 5f), dir.angle() - 180f)
            if (abs(getAngleToPlayer(playerSpace, dir, space)) > 20f) {
                animController.forceCurrent(rotate)
            }
        }

        rotate.onFrame[1] = {
            blinkLight(world, draw, dir, light, space)
        }
        rotate.onFrame[2] = {
            light.updateIntesity(highLightIntensity * 0.25f)
        }
        rotate.onFrame[3] = {
            light.updateIntesity(baseLightIntensity)
        }
        rotate.onFrame[18] = {
            dir.rotate(1f)
        }
        rotate.onFrame[19] = {
            dir.rotate(2f)
        }
        rotate.onFrame[20] = {
            dir.rotate(2f)
        }
        rotate.onFrame[21] = {
            dir.rotate(7f)
        }
        rotate.onFrame[22] = {
            dir.rotate(3f)
        }
        rotate.lastOnFunction {
            println("angle to player: ${abs(getAngleToPlayer(playerSpace, dir, space))}")
            if (abs(getAngleToPlayer(playerSpace, dir, space)) < 10f)
                animController.forceCurrent(shoot)
        }

        spawn.lastOnFunction {
            animController.forceCurrent(rotate)
            setupCollider(collider, false)
        }

        setupDraw(draw, animController, dir)

        onDeadEmitter.emit = {
            GSounds.explosion1.play()
            for (i in 0..10)
                SParticles.explosionParticle(world, space.centerX, space.centerY, 22f, SParticles.whiteAnim, GPalette.WHITE)
            for (i in 0..30)
                SParticles.explosionParticle(world, space.centerX, space.centerY, 22f, SParticles.lavenderAnim, GPalette.LAVENDER)
        }
    }

    private fun getAngleToPlayer(playerSpace: CompSpace, dir: Vector2, space: CompSpace): Float {
        return tempVector.set(playerSpace.centerX - space.centerX, playerSpace.centerY - space.centerY).angle() - (dir.angle() + 180f)
    }

    private fun blinkLight(world: World, draw: CompDraw, dir: Vector2, light: GLight, space: CompSpace) {
        tempVector.set(0f, 2f).setAngle(dir.angle() + 180f)
        light.updatePos(space.centerX + tempVector.x, space.y + tempVector.y)
        light.updateIntesity(highLightIntensity)
        SParticles.gunnerHearthParticle(world, space, draw.angle)
    }

    private fun setupCollider(collider: CompCollide, spawn: Boolean) {
        if (spawn) {
            collider.setIds(Ids.enemy)
            collider.addCollidingWith(Ids.playerBullet)
            collider.setDmgToInflict(5)
        } else {
            collider.setIds(Ids.enemy)
            collider.addCollidingWith(Ids.playerBullet, Ids.player)
            collider.setDmgToInflict(5)
        }
    }

    private fun setupDraw(draw: CompDraw, animController: GAnimController, dir: Vector2) {
        draw.preDraw = {
            draw.currentImg = animController.getFrame()
            draw.angle = dir.angle() + 90f
            animController.update(GTime.enemyDelta)
        }
        draw.drawFront = { batch, space ->
            batch.draw(draw.currentImg.front,
                    (space.centerX - draw.currentImg.front.hw).roundToInt().toFloat(), (space.centerY - draw.currentImg.front.hh).roundToInt().toFloat(),
                    draw.currentImg.front.hw, draw.currentImg.front.hh - 7,
                    draw.currentImg.front.w, draw.currentImg.front.h,
                    1f, 1f, draw.angle)
        }
        draw.drawNormal = { batch, space ->
            batch.draw(draw.currentImg.normal,
                    (space.centerX - draw.currentImg.front.hw).roundToInt().toFloat(), (space.centerY - draw.currentImg.front.hh).roundToInt().toFloat(),
                    draw.currentImg.front.hw, draw.currentImg.front.hh - 7,
                    draw.currentImg.front.w, draw.currentImg.front.h,
                    1f, 1f, draw.angle)
        }
        draw.drawOcc = { batch, space ->
            batch.draw(draw.currentImg.occluder,
                    (space.centerX - draw.currentImg.front.hw).roundToInt().toFloat(), (space.centerY - draw.currentImg.front.hh).roundToInt().toFloat(),
                    draw.currentImg.front.hw, draw.currentImg.front.hh - 7,
                    draw.currentImg.front.w, draw.currentImg.front.h,
                    1f, 1f, draw.angle)
        }
    }


}
