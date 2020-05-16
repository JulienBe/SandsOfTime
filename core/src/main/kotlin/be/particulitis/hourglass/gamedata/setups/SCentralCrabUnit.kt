package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GHistoryFloat
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GPalette
import be.particulitis.hourglass.common.puppet.GAnimController
import be.particulitis.hourglass.gamedata.graphics.Frames
import be.particulitis.hourglass.common.puppet.GAnim
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.*
import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs
import kotlin.math.roundToInt

object SCentralCrabUnit : Setup() {

    private const val slugTrailSize = 5
    private const val baseLightIntensity = 0.03f
    private const val highLightIntensity = 0.075f
    private val baseLightColor = GPalette.WHITE
    private val highLightColor = GPalette.RED

    fun setup(world: World, baseX: Float, baseY: Float) {
        val enemy = world.create(Builder.enemyCpu)
//        SParticles.spawnTransition(world, baseX + 6, baseY + 6, enemy.hp())
        val player = world.getSystem(TagManager::class.java).getEntity(Data.playerTag)
        val space = enemy.space()
        val draw = enemy.draw()
        val dir = Vector2(5f, 0f)
        space.setDim(Dim.Enemy.w, Dim.Enemy.w)
        space.setPos(baseX, baseY)
        enemy.collide().setIds(Ids.enemy)
        enemy.collide().addCollidingWith(Ids.player, Ids.playerBullet)
        enemy.collide().setDmgToInflict(2)
        draw.layer = Data.enemyLayer
        enemy.layer().setLayer(Phases.Enemy)
        val spawn = GAnim(Frames.CPU_SPAWN, 0.1f, Animation.PlayMode.NORMAL)
        val idle = GAnim(Frames.CPU_IDLE)
        val walk = GAnim(Frames.CPU_WALK)
        val jump = GAnim(Frames.CPU_JUMPING)
        val animController = GAnimController(spawn)
        val light = GLight(space.centerX, space.centerY + 1f, baseLightIntensity)
        blinkCpu(world, space, dir.angle(), light)

        enemy.hp().setHp(3)
        enemy.hp().onDead = {
            light.clear()
        }

        setupSpawn(light, spawn, idle, animController)
        setupIdle(idle, dir, light, space, walk, animController, player)
        setupWalk(walk, world, space, dir, light, jump, animController, player, idle)
        setupJump(jump, enemy, dir, world, space, player, light)

        val posX = GHistoryFloat(slugTrailSize)
        val posY = GHistoryFloat(slugTrailSize)
        draw.currentImg = GGraphics.red
        draw.preDraw = {
            light.updatePos(space.centerX, space.centerY)
            posX.add(space.x.roundToInt().toFloat())
            posY.add(space.y.roundToInt().toFloat())
            draw.currentImg = animController.getFrame()
            draw.angle = dir.angle() + 90f
            if (animController.current == spawn)
                animController.update(GTime.delta)
            else
                animController.update(GTime.enemyDelta)
        }
        draw.drawFront = { batch, space ->
            batch.drawCenteredOnBox(draw, space, draw.currentImg.front)
        }
        draw.drawNormal = { batch, space ->
            batch.drawCenteredOnBox(draw, space, draw.currentImg.normal)
        }
        draw.drawOcc = { batch, space ->
            batch.drawCenteredOnBox(draw, space, draw.currentImg.occluder)
        }

        enemy.emitter().emit = {
            for (i in 0..40)
                SParticles.explosionParticle(world, space.centerX, space.centerY, 28f)
        }
    }

    private fun setupSpawn(light: GLight, spawn: GAnim, idle: GAnim, animController: GAnimController) {
        spawn.onFrame[2] = { normalLight(light) }
        spawn.onFrame[3] = { normalLight(light) }
        spawn.onFrame[4] = { normalLight(light) }
        spawn.onFrame[5] = { normalLight(light) }
        spawn.lastOnFunction {
            animController.forceCurrent(idle)
        }
    }

    private fun setupJump(jump: GAnim, entity: Entity, dir: Vector2, world: World, space: CompSpace, player: Entity, light: GLight) {
        jump.playMode = Animation.PlayMode.NORMAL
        jump.onFrame.set(0) { blinkCpu(world, space, dir.angle(), light) }
        jump.onFrame.set(1) { normalLight(light) }
        jump.onFrame.set(2) { blinkCpu(world, space, dir.angle(), light) }
        jump.onFrame.set(3) { normalLight(light) }
        jump.onFrame.set(4) { blinkCpu(world, space, dir.angle(), light) }
        jump.onFrame.set(5) { normalLight(light) }
        jump.onFrame.set(6) { blinkCpu(world, space, dir.angle(), light) }
        jump.onFrame.set(7) { normalLight(light) }
        jump.onFrame.set(8) { blinkCpu(world, space, dir.angle(), light) }
        jump.onFrame.set(9) {
            space.move(dir.x, dir.y, GTime.enemyDelta * 10f)
        }
        jump.inFrame[9] = {
            space.move(dir.x, dir.y, GTime.enemyDelta * 10f)
        }
        jump.inFrame[10] = jump.inFrame[9]
        jump.inFrame[11] = jump.inFrame[10]
    }

    private fun normalLight(light: GLight) {
        light.updateIntesityRGB(baseLightIntensity, baseLightColor)
    }

    private fun setupIdle(idle: GAnim, dir: Vector2, light: GLight, space: CompSpace, walk: GAnim, animController: GAnimController, player: Entity) {
        idle.onFrame.set(0) {
            normalLight(light)
            testIdleToWalk(walk, animController, player, dir, space)
        }
        idle.onFrame.set(1) {
            normalLight(light)
            turnTowardPlayer(space, dir, player.space())
        }
        idle.onFrame.set(2) {
            normalLight(light)
            testIdleToWalk(walk, animController, player, dir, space)
        }
        idle.onFrame.set(3) {
            normalLight(light)
            turnTowardPlayer(space, dir, player.space())
        }
    }

    private fun setupWalk(walk: GAnim, world: World, space: CompSpace, dir: Vector2, light: GLight, jump: GAnim, animController: GAnimController, player: Entity, idle: GAnim) {
        walk.onFrame.set(0) {
            normalLight(light)
            testWalkToJump(jump, animController, player, space)
            testWalkToIdle(idle, animController, player, dir, space)
        }
        walk.onFrame[4] = walk.onFrame[0]
        walk.onFrame[3] = {
            space.move(dir.x, dir.y, GTime.enemyDelta * 20f)
            val angle = dir.angle()
            blinkCpu(world, space, angle, light)
            spawnFootstep(2f, -7f, angle, space, world)
            spawnFootstep(2f, -6f, angle, space, world)
            spawnFootstep(-2f, 5f, angle, space, world)
            spawnFootstep(-2f, 6f, angle, space, world)
        }
        walk.onFrame[7] = {
            space.move(dir.x, dir.y, GTime.enemyDelta * 20f)
            val angle = dir.angle()
            blinkCpu(world, space, angle, light)
            spawnFootstep(-2f, -7f, angle, space, world)
            spawnFootstep(-2f, -6f, angle, space, world)
            spawnFootstep(2f, 5f, angle, space, world)
            spawnFootstep(2f, 6f, angle, space, world)
        }
    }

    private fun blinkCpu(world: World, space: CompSpace, angle: Float, light: GLight) {
        light.updateIntesityRGB(highLightIntensity, highLightColor)
        SParticles.cpuHearthParticle(world, space.centerX - 1f, space.centerY - 1f, 2f, 2f, angle)
    }

    private val offset = Vector2()
    private fun spawnFootstep(x: Float, y: Float, angle: Float, space: CompSpace, world: World) {
        offset.set(-y, x).rotate(angle)
        SParticles.cpuFootstep(world, space.centerX + offset.x, space.centerY + offset.y)
    }

    private val dstVector = Vector2()
    private fun testWalkToJump(anim: GAnim, animController: GAnimController, player: Entity, space: CompSpace): Boolean {
        if (dstVector.set(player.space().centerX - space.centerX, player.space().centerY - space.centerY).len2() < 1500f) {
            animController.forceCurrent(anim)
            return true
        }
        return false
    }

    private fun testIdleToWalk(walk: GAnim, animController: GAnimController, player: Entity, dir: Vector2, space: CompSpace) {
        if (abs(getAngleToPlayer(player.space(), dir, space)) < 5)
            animController.forceCurrent(walk)
    }

    private fun testWalkToIdle(idle: GAnim, animController: GAnimController, player: Entity, dir: Vector2, space: CompSpace) {
        if (abs(getAngleToPlayer(player.space(), dir, space)) > 5)
            animController.forceCurrent(idle)
    }

    private val turnVector = Vector2()
    private fun turnTowardPlayer(space: CompSpace, dir: Vector2, playerSpace: CompSpace) {
        val playerDistance = turnVector.set(playerSpace.centerX - space.centerX, playerSpace.centerY - space.centerY).len()
        val dirPreviousLen2 = dir.len2()
        dir.nor()
        // where will I be if I continue
        turnVector.set(space.centerX + (dir.x * playerDistance), space.centerY + (dir.y * playerDistance))
        // offset of where I want to be
        turnVector.set(playerSpace.centerX - turnVector.x, playerSpace.centerY - turnVector.y)
        // don't move too much
        if (turnVector.len2() > 1f) {
            turnVector.nor()
        }
        dir.add(turnVector.x * GTime.enemyDelta * 8f, turnVector.y * GTime.enemyDelta * 8f)
        dir.setLength2(dirPreviousLen2)
    }

    private val angleVector = Vector2()
    private fun getAngleToPlayer(playerSpace: CompSpace, dir: Vector2, space: CompSpace): Float {
        return angleVector.set(playerSpace.centerX - space.centerX, playerSpace.centerY - space.centerY).angle() - dir.angle()
    }

}