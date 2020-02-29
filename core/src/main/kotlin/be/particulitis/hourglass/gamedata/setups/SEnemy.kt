package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.puppet.GAnimController
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
        val idle = GAnimN("cpu")
        val walk = GAnimN("cpu_walk")
        val jump = GAnimN("cpu_jumping")
        val attack = GAnimN("cpu_attack")
        val animController = GAnimController(idle, idle)
        val lightId = if (light) GLight(space.centerX, space.centerY + 1f, baseLightIntensity).id else -1
        val lightOffsetIdleF0F2 = Vector2(0f, 0f)
        val lightOffsetIdleF1 = Vector2(0f, 0f)
        val lightOffsetIdleF3 = Vector2(0f, 0f)

        enemy.hp().setHp(10)
        enemy.hp().onDead = {
            GLight.clearCheck(lightId)
        }
        enemy.collide().setDmgToInflict(3)

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
        jump.frames.last().onFrame = {
            animController.forceCurrent(attack)
        }
        jump.frames[1].onFrame = { blinkCpu(world, space, dir.angle()) }
        jump.frames[11].onFrame = { blinkCpu(world, space, dir.angle()) }
    }

    private fun setupAttack(attack: GAnimN, dir: Vector2, lightId: Int, space: CompSpace, world: World, animController: GAnimController, walk: GAnimN) {
        attack.frames[0].onFrame = { blinkCpu(world, space, dir.angle()) }
        attack.frames[5].onFrame = { blinkCpu(world, space, dir.angle()) }
        attack.frames[14].onFrame = { blinkCpu(world, space, dir.angle()) }
        attack.frames[16].onFrame = { blinkCpu(world, space, dir.angle()) }
        attack.frames[18].onFrame = { blinkCpu(world, space, dir.angle()) }
        attack.frames[20].onFrame = { blinkCpu(world, space, dir.angle()) }
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
                GLight.updateIntensityCheck(lightId, easeAttackRotation.apply(baseLightIntensity + 0.2f, highLightIntensity + 0.2f, attack.time))
            else
                GLight.updateIntensityCheck(lightId, easeAttackRotation.apply(highLightIntensity + 0.2f, baseLightIntensity + 0.2f, attack.time - 1f))
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
        idle.frames[0].onFrame = {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            testIdleToWalk(walk, animController, player, dir, space)
        }
        idle.frames[1].onFrame = {
            updateMainLight(lightOffsetIdleF1, dir, lightId, space)
            turnTowardPlayer(space, dir, player.space())
        }
        idle.frames[2].onFrame = {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            testIdleToWalk(walk, animController, player, dir, space)
        }
        idle.frames[3].onFrame = {
            turnTowardPlayer(space, dir, player.space())
            updateMainLight(lightOffsetIdleF3, dir, lightId, space)
        }
    }

    private fun setupWalk(walk: GAnimN, world: World, space: CompSpace, dir: Vector2, lightOffsetIdleF0F2: Vector2, lightId: Int, jump: GAnimN, animController: GAnimController, player: Entity, idle: GAnimN) {
        walk.frames[0].onFrame = {
            val angle = dir.angle()
            blinkCpu(world, space, angle)
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            GLight.updateIntensityCheck(lightId, highLightIntensity)
            testWalkToJump(jump, animController, player, space)
            testWalkToJump(jump, animController, player, space)
            spawnFootstep(-7f, 26f, angle, space, world)
            spawnFootstep(-8f, 25f, angle, space, world)
            spawnFootstep(-8f, 24f, angle, space, world)
            spawnFootstep(-8f, 23f, angle, space, world)
        }
        walk.frames[1].onFrame = {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            GLight.updateIntensityCheck(lightId, baseLightIntensity)
            if (!testWalkToJump(jump, animController, player, space))
                testWalkToIdle(idle, animController, player, dir, space)
        }
        walk.frames[5].onFrame = {
            val angle = dir.angle()
            spawnFootstep(7f, 31f, angle, space, world)
            spawnFootstep(7f, 30f, angle, space, world)
            spawnFootstep(7f, 29f, angle, space, world)
            spawnFootstep(7f, 28f, angle, space, world)
            spawnFootstep(8f, 28f, angle, space, world)
        }
        walk.frames[6].onFrame = {
            val angle = dir.angle()
            blinkCpu(world, space, angle)
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            GLight.updateIntensityCheck(lightId, highLightIntensity)
            testWalkToJump(jump, animController, player, space)
            spawnFootstep(7f, 26f, angle, space, world)
            spawnFootstep(8f, 25f, angle, space, world)
            spawnFootstep(8f, 24f, angle, space, world)
            spawnFootstep(8f, 23f, angle, space, world)
        }
        walk.frames[2].inFrame = {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            space.move(dir.x, dir.y, GTime.enemyDelta)
        }
        walk.frames[3].inFrame = {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            space.move(dir.x, dir.y, GTime.enemyDelta * 2f)
        }
        walk.frames[4].inFrame = {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            space.move(dir.x, dir.y, GTime.enemyDelta * 7f)
        }
        walk.frames[5].inFrame = {
            updateMainLight(lightOffsetIdleF0F2, dir, lightId, space)
            space.move(dir.x, dir.y, GTime.enemyDelta * 2f)
        }
        walk.frames[7].onFrame = walk.frames[1].onFrame
        walk.frames[8].inFrame = walk.frames[2].inFrame
        walk.frames[9].inFrame = walk.frames[3].inFrame
        walk.frames[10].inFrame = walk.frames[4].inFrame
        walk.frames[11].inFrame = walk.frames[5].inFrame
        walk.frames[11].onFrame = {
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