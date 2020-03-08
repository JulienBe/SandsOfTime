package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.puppet.GAnimController
import be.particulitis.hourglass.common.puppet.Frames
import be.particulitis.hourglass.common.puppet.GAnimN
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.*
import be.particulitis.hourglass.gamedata.graphics.Colors
import com.artemis.Entity
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import kotlin.math.abs

object SEnemy : Setup() {

    val baseLightIntensity = 0.01f
    val highLightIntensity = 0.1f
    var light = false

    fun enemySlug(world: World, baseX: Float, baseY: Float) {
        val enemy = world.create(Builder.enemyCpu)
        SParticles.spawnTransition(world, baseX + 6, baseY + 6, enemy.hp())
        val player = world.getSystem(TagManager::class.java).getEntity(Data.playerTag)
        val space = enemy.space()
        val draw = enemy.draw()
        val dir = Vector2(5f, 0f)
        space.setDim(Dim.Enemy.w, Dim.Enemy.w)
        space.setPos(baseX, baseY)
        enemy.collide().setIds(Ids.enemy)
        enemy.collide().addCollidingWith(Ids.player, Ids.playerBullet)
        enemy.layer().setLayer(Phases.Enemy)
        draw.color = Colors.enemy
        draw.layer = Data.enemyLayer
        enemy.collide().setDmgToInflict(2)
        val spawn = GAnimN(Frames.CPU_SPAWN)
        val idle = GAnimN(Frames.CPU_IDLE)
        val walk = GAnimN(Frames.CPU_WALK)
        val jump = GAnimN(Frames.CPU_JUMPING)
        val attack = GAnimN(Frames.CPU_ATTACK)
        val animController = GAnimController(spawn)
        val lightId = if (light) GLight(space.centerX, space.centerY + 1f, baseLightIntensity).id else -1
        val lightOffsetIdleF0F2 = Vector2(0f, 0f)
        val lightOffsetIdleF1 = Vector2(0f, 0f)
        val lightOffsetIdleF3 = Vector2(0f, 0f)
        blinkCpu(world, space, dir.angle())

        enemy.hp().setHp(30)
        enemy.hp().onDead = {
            GLight.clear(lightId)
        }
        enemy.collide().setDmgToInflict(3)

        spawn.lastOnFunction {
            animController.forceCurrent(idle)
        }
        setupIdle(idle, lightOffsetIdleF0F2, dir, lightId, space, walk, animController, player, lightOffsetIdleF1, lightOffsetIdleF3)
        setupWalk(walk, world, space, dir, lightOffsetIdleF0F2, lightId, jump, animController, player, idle)
        setupJump(jump, animController, attack, dir, world, space)
        setupAttack(attack, dir, lightId, space, world, animController, walk)

        draw.preDraw = {
            draw.currentImg = animController.getFrame()
            draw.angle = dir.angle() + 90f
            animController.update(GTime.enemyDelta)
        }

        enemy.emitter().emit = {
            for (i in 0..40)
                SParticles.explosionParticle(world, space.centerX, space.centerY, 28f)
        }
    }

    private fun setupJump(jump: GAnimN, animController: GAnimController, attack: GAnimN, dir: Vector2, world: World, space: CompSpace) {
        jump.playMode = Animation.PlayMode.NORMAL
        jump.lastOnFunction {
            animController.forceCurrent(attack)
        }
        jump.onFrame.set(1) { blinkCpu(world, space, dir.angle()) }
        jump.onFrame.set(11) { blinkCpu(world, space, dir.angle()) }
    }

    private fun setupAttack(attack: GAnimN, dir: Vector2, lightId: Int, space: CompSpace, world: World, animController: GAnimController, walk: GAnimN) {
        attack.onFrame.set(0) { blinkCpu(world, space, dir.angle()) }
        attack.onFrame.set(5) { blinkCpu(world, space, dir.angle()) }
        attack.onFrame.set(14) { blinkCpu(world, space, dir.angle()) }
        attack.onFrame.set(16) { blinkCpu(world, space, dir.angle()) }
        attack.onFrame.set(18) { blinkCpu(world, space, dir.angle()) }
        attack.onFrame.set(20) { blinkCpu(world, space, dir.angle()) }
        val easeAttackRotation = Interpolation.circle
        var attackEmitCpt = 0
        var originalAngle = 0f
        attack.onStart = {
            originalAngle = dir.angle()
        }
        attack.inEachFrame {
            dir.setAngle(easeAttackRotation.apply(originalAngle, originalAngle + 1080f, attack.time / 2f))
            val angle = dir.angle()
            if (attack.time < 1f)
                GLight.updateIntensity(lightId, easeAttackRotation.apply(baseLightIntensity + 0.2f, highLightIntensity + 0.2f, attack.time))
            else
                GLight.updateIntensity(lightId, easeAttackRotation.apply(highLightIntensity + 0.2f, baseLightIntensity + 0.2f, attack.time - 1f))
            when (attackEmitCpt) {
                1 -> {
                    spawnParticle(11f, 13f, angle, space, world)
                    spawnParticle(12f, 13f, angle, space, world)
                    spawnParticle(12f, 14f, angle, space, world)
                    spawnParticle(12f, 15f, angle, space, world)

                    spawnParticle(19f, 25f, angle, space, world)
                }
                2 -> {
                    spawnParticle(13f, 15f, angle, space, world)
                    spawnParticle(13f, 16f, angle, space, world)
                    spawnParticle(13f, 17f, angle, space, world)
                    spawnParticle(14f, 17f, angle, space, world)
                }
                3 -> {
                    spawnParticle(14f, 18f, angle, space, world)
                    spawnParticle(14f, 19f, angle, space, world)
                    spawnParticle(15f, 19f, angle, space, world)
                    spawnParticle(15f, 20f, angle, space, world)
                }
                4 -> {
                    spawnParticle(15f, 21f, angle, space, world)
                    spawnParticle(16f, 21f, angle, space, world)
                    spawnParticle(16f, 22f, angle, space, world)
                    spawnParticle(16f, 23f, angle, space, world)

                    spawnParticle(19f, 25f, angle, space, world)
                }
            }

            spawnParticle(17f, 23f, angle, space, world)
            spawnParticle(17f, 24f, angle, space, world)
            spawnParticle(20f, 26f, angle, space, world)
            spawnParticle(18f, 24f, angle, space, world)
            attackEmitCpt = ++attackEmitCpt % 9
            if (animController.current.time > 1.9f)
                animController.forceCurrent(walk)
        }
    }

    private fun setupIdle(idle: GAnimN, lightOffsetIdleF0F2: Vector2, dir: Vector2, lightId: Int, space: CompSpace, walk: GAnimN, animController: GAnimController, player: Entity, lightOffsetIdleF1: Vector2, lightOffsetIdleF3: Vector2) {
        idle.onFrame.set(0) {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            testIdleToWalk(walk, animController, player, dir, space)
        }
        idle.onFrame.set(1) {
            updateMainLight(lightOffsetIdleF1, dir, lightId, space)
            turnTowardPlayer(space, dir, player.space())
        }
        idle.onFrame.set(2) {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            testIdleToWalk(walk, animController, player, dir, space)
        }
        idle.onFrame.set(3) {
            turnTowardPlayer(space, dir, player.space())
            updateMainLight(lightOffsetIdleF3, dir, lightId, space)
        }
    }

    private fun setupWalk(walk: GAnimN, world: World, space: CompSpace, dir: Vector2, lightOffsetIdleF0F2: Vector2, lightId: Int, jump: GAnimN, animController: GAnimController, player: Entity, idle: GAnimN) {
        walk.onFrame.set(0) {
            val angle = dir.angle()
            blinkCpu(world, space, angle)
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            GLight.updateIntensity(lightId, highLightIntensity)
            testWalkToJump(jump, animController, player, space)
            testWalkToJump(jump, animController, player, space)
            spawnFootstep(-7f, 26f, angle, space, world)
            spawnFootstep(-8f, 25f, angle, space, world)
            spawnFootstep(-8f, 24f, angle, space, world)
            spawnFootstep(-8f, 23f, angle, space, world)
        }
        walk.onFrame.set(1) {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            GLight.updateIntensity(lightId, baseLightIntensity)
            if (!testWalkToJump(jump, animController, player, space))
                testWalkToIdle(idle, animController, player, dir, space)
        }
        walk.onFrame.set(5) {
            val angle = dir.angle()
            spawnFootstep(7f, 31f, angle, space, world)
            spawnFootstep(7f, 30f, angle, space, world)
            spawnFootstep(7f, 29f, angle, space, world)
            spawnFootstep(7f, 28f, angle, space, world)
            spawnFootstep(8f, 28f, angle, space, world)
        }
        walk.onFrame.set(6) {
            val angle = dir.angle()
            blinkCpu(world, space, angle)
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            GLight.updateIntensity(lightId, highLightIntensity)
            testWalkToJump(jump, animController, player, space)
            spawnFootstep(7f, 26f, angle, space, world)
            spawnFootstep(8f, 25f, angle, space, world)
            spawnFootstep(8f, 24f, angle, space, world)
            spawnFootstep(8f, 23f, angle, space, world)
        }
        walk.inFrame.set(2) {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            space.move(dir.x, dir.y, GTime.enemyDelta)
        }
        walk.inFrame.set(3) {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            space.move(dir.x, dir.y, GTime.enemyDelta * 2f)
        }
        walk.inFrame.set(4) {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            space.move(dir.x, dir.y, GTime.enemyDelta * 7f)
        }
        walk.inFrame.set(5) {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            space.move(dir.x, dir.y, GTime.enemyDelta * 2f)
        }
        walk.onFrame[7] = walk.onFrame[1]
        walk.inFrame[8] = walk.inFrame[2]
        walk.inFrame[9] = walk.inFrame[3]
        walk.inFrame[10] = walk.inFrame[4]
        walk.inFrame[11] = walk.inFrame[5]
        walk.onFrame[11] = {
            val angle = dir.angle()
            spawnFootstep(-7f, 31f, angle, space, world)
            spawnFootstep(-7f, 30f, angle, space, world)
            spawnFootstep(-7f, 29f, angle, space, world)
            spawnFootstep(-7f, 28f, angle, space, world)
            spawnFootstep(-8f, 28f, angle, space, world)
        }
    }

    private fun blinkCpu(world: World, space: CompSpace, angle: Float) {
        SParticles.cpuHearthParticle(world, space.centerX - 3.5f, space.centerY - 3.5f, 7f, 7f, angle)
    }

    private val offset = Vector2()
    private fun spawnParticle(x: Float, y: Float, angle: Float, space: CompSpace, world: World) {
        offset.set(y, x).rotate(angle)
        SParticles.cpuAttackParticle(world, space.centerX + offset.x, space.centerY + offset.y)
        SParticles.cpuAttackParticle(world, space.centerX - offset.x, space.centerY - offset.y)
        offset.set(-y, x).rotate(angle)
        SParticles.cpuAttackParticle(world, space.centerX + offset.x, space.centerY + offset.y)
        SParticles.cpuAttackParticle(world, space.centerX - offset.x, space.centerY - offset.y)
    }
    private fun spawnFootstep(x: Float, y: Float, angle: Float, space: CompSpace, world: World) {
        offset.set(-y, x).rotate(angle)
        SParticles.cpuFootstep(world, space.centerX + offset.x, space.centerY + offset.y)
        SParticles.cpuFootstep(world, space.centerX - offset.x, space.centerY - offset.y)
    }

    private val dstVector = Vector2()
    private fun testWalkToJump(anim: GAnimN, animController: GAnimController, player: Entity, space: CompSpace): Boolean {
        if (dstVector.set(player.space().centerX - space.centerX, player.space().centerY - space.centerY).len2() < 1500f) {
            animController.forceCurrent(anim)
            return true
        }
        return false
    }

    private fun testIdleToWalk(walk: GAnimN, animController: GAnimController, player: Entity, dir: Vector2, space: CompSpace) {
        if (abs(getAngleToPlayer(player.space(), dir, space)) < 5)
            animController.forceCurrent(walk)
    }

    private fun testWalkToIdle(idle: GAnimN, animController: GAnimController, player: Entity, dir: Vector2, space: CompSpace) {
        if (abs(getAngleToPlayer(player.space(), dir, space)) > 5)
            animController.forceCurrent(idle)
    }

    private fun updateMainLight(offset: Vector2, dir: Vector2, lightId: Int, space: CompSpace) {
        if (lightId != -1) {
            offset.setAngle(dir.angle() + 90f)
            GLight.updatePos(lightId, space.centerX + offset.x, space.centerY + offset.y)
        }
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
        dir.add(turnVector.x * GTime.enemyDelta * 16f, turnVector.y * GTime.enemyDelta)
        dir.setLength2(dirPreviousLen2)
    }

    private val angleVector = Vector2()
    private fun getAngleToPlayer(playerSpace: CompSpace, dir: Vector2, space: CompSpace): Float {
        return angleVector.set(playerSpace.centerX - space.centerX, playerSpace.centerY - space.centerY).angle() - dir.angle()
    }

}