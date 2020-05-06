package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.Phases
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.system.SysCollider
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2

object SPlayer : Setup() {

    private const val playerSpeed = 150f
    private const val playerLayer = 10

    fun player(world: World, offsetX: Float = 0f, offsetY: Float = 0f) {
        val player = world.create(Builder.player)
        world.getSystem(TagManager::class.java).register(Data.playerTag, player.id)

        val draw = player.draw()
        val space = player.space()
        val shoot = player.shooter()
        var shootLeft = true

        player.control().addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        player.control().addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        player.control().addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        player.control().addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        player.hp().setHp(100)
        player.space().setDim(Dim.Player)
        player.space().setPos(offsetX + GResolution.areaHW - Dim.Player.hw, offsetY + GResolution.areaHH - Dim.Player.hw)

//        val shootAnim = GAnim(Frames.PLAYER_SHOOT, 0.05f)
//        val defaultAnim = GAnim(Frames.PLAYER_IDLE)
//        val animController = GAnimController(defaultAnim)

        shoot.setFirerate(0.25f)
        shoot.setOffset(Dim.PlayerSprite.hw - Dim.Bullet.hw, Dim.PlayerSprite.hh - Dim.Bullet.hw)
        shoot.setKey(Input.Keys.SPACE)
        shoot.shouldShood = {
            !shoot.keyCheck || (shoot.keyCheck && GKeyGlobalState.touched)
        }

        val wizLight = GLight(space.centerX, space.centerY, 0.15f)
        val mainLight = GLight(space.x + 1f, space.centerY + 6f, 0.2f)
        val leftLight = GLight(space.x + 1f, space.centerY + 6f, 0.0f, 0f, 1.1f, 0.2f, 1f, 1f)
        val rightLight = GLight(space.x + 1f, space.centerY + 6f, 0.0f, 0f, 1.1f, 1f, 0.2f, 0.2f)
        var leftIntensity = 0f
        var rightIntensity = 0f
        shoot.shootingFunc = {
            val position = getShootOffsetFromCenter(GHelper.x, GHelper.y, space, shootLeft)
            shoot.dir.set(GHelper.x - space.centerX, GHelper.y - space.centerY)
            shoot.dir.nor()
            shoot.bullet.second.invoke(world, space.centerX + position.x, space.centerY +  position.y, shoot.dir, 1)
            for (i in 0..15)
                SParticles.muzzle(world, space.centerX + position.x, space.centerY +  position.y, shoot.dir)
            if (shootLeft)
                leftIntensity = .20f
            else
                rightIntensity = .20f
            shootLeft = !shootLeft
        }
        shoot.setBullet(Builder.bullet, SBullet::playerBullet)

        player.collide().setIds(Ids.player)
        player.collide().addCollidingWith(Ids.enemy, Ids.enemyBullet, Ids.propsWall)
        player.collide().collidingMap.put(Ids.propsWall.id, world.getSystem(SysCollider::class.java)::rollback)

        player.charMvt().speed = playerSpeed
        player.layer().setLayer(Phases.Player)

        draw.layer = playerLayer
        val angleVector = Vector2()
        val angleRandomness = GPeriodicValue(0.1f) {
            GRand.nextGaussian().toFloat()
        }
        val intensityRandomness = GPeriodicValue(0.11f) {
            GRand.nextGaussian().toFloat() / 1000f
        }
        val tiltRandomness = GPeriodicValue(0.09f) {
            GRand.nextGaussian().toFloat() / 40f
        }
        draw.currentImg = GGraphics.blue
        draw.preDraw = {
//            animController.update(GTime.playerDelta)
            if (!GTime.enemyPhase) {
                draw.angle = angleVector.set(space.centerX - GHelper.x, space.centerY - GHelper.y).angle() + 90f
                angleVector.nor()
                wizLight.updatePos(space.centerX, space.centerY)
                mainLight.updateTilt(2f + tiltRandomness.value)
                angleRandomness.tick(GTime.playerDelta)
                intensityRandomness.tick(GTime.playerDelta)
//                draw.currentImg = animController.getFrame()

                leftIntensity = updateShootLight(space, leftLight, draw.angle - 90f, leftIntensity, true)
                rightIntensity = updateShootLight(space, rightLight, draw.angle - 90f, rightIntensity, false)
//                if (GKeyGlobalState.justTouched) {
//                    animController.forceCurrent(shootAnim)
//                }
            }
            mainLight.updatePosAngle(space.centerX - angleVector.x * 20f, space.centerY - angleVector.y * 20f, draw.angle + angleRandomness.value - 90f)
            mainLight.updateIntesity(0.2f + intensityRandomness.value)
            wizLight.updateIntesity(0.15f + intensityRandomness.value)
//            if (!GKeyGlobalState.touched && animController.current == shootAnim) {
//                animController.forceCurrent(defaultAnim)
//            }
        }
    }

    private fun updateShootLight(space: CompSpace, light: GLight, angle: Float, intensity: Float, left: Boolean): Float {
        val pos = getShootOffsetFromCenter(GHelper.x, GHelper.y, space, left)
        light.updatePosAngle(space.centerX + pos.x, space.centerY + pos.y, angle)
        light.updateIntesity(intensity)
        return (intensity / 1.2f)
    }

    val shootOffset = Vector2(0f, 18f)
    val angleCompute = Vector2()
    fun getShootOffsetFromCenter(targetX: Float, targetY: Float, space: CompSpace, left: Boolean): Vector2 {
        val angle = angleCompute.set(targetX - space.centerX, targetY - space.centerY).angleRad()
        return if (left)
            shootOffset.setAngleRad(angle - .22f)
        else
            shootOffset.setAngleRad(angle + .22f)
    }

}