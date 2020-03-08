package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.common.drawing.GLight
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.common.puppet.Frames
import be.particulitis.hourglass.common.puppet.GAnimN
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
    private var shootLeft = true

    fun player(world: World, offsetX: Float = 0f, offsetY: Float = 0f) {
        val player = world.create(Builder.player)
        world.getSystem(TagManager::class.java).register(Data.playerTag, player.id)

        val draw = player.draw()
        val space = player.space()
        val shoot = player.shooter()

        player.control().addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        player.control().addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        player.control().addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        player.control().addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        player.hp().setHp(100)
        player.space().setDim(Dim.Player)
        player.space().setPos(offsetX + GResolution.areaHW - Dim.Player.hw, offsetY + GResolution.areaHH - Dim.Player.hw)
        val shootAnim = GAnimN(Frames.PLAYER_SHOOT)
        val defaultAnim = GAnimN(Frames.PLAYER_IDLE)
        var currentAnim = defaultAnim
        shoot.setFirerate(0.25f)
        shoot.setOffset(Dim.PlayerSprite.hw - Dim.Bullet.hw, Dim.PlayerSprite.hh - Dim.Bullet.hw)
        shoot.setKey(Input.Keys.SPACE)
        shoot.shouldShood = {
            !shoot.keyCheck || (shoot.keyCheck && GKeyGlobalState.touched)
        }

        shoot.shootingFunc = {
            val position = getShootOffsetFromCenter(GHelper.x, GHelper.y, space)
            shoot.dir.set(GHelper.x - space.centerX, GHelper.y - space.centerY)
            shoot.dir.nor()
            shootLeft = !shootLeft
            val bulletEntity = shoot.bullet.second.invoke(world, space.centerX + position.x, space.centerY +  position.y, shoot.dir, 1)
            for (i in 0..15)
                SParticles.muzzle(world, space.centerX + position.x, space.centerY +  position.y, shoot.dir)
        }
        shoot.setBullet(Builder.bullet, SBullet::playerBullet)

        player.collide().setIds(Ids.player)
        player.collide().addCollidingWith(Ids.enemy, Ids.enemyBullet, Ids.propsWall)
        player.collide().collidingMap.put(Ids.propsWall.id, world.getSystem(SysCollider::class.java)::rollback)

        player.charMvt().speed = playerSpeed
        player.layer().setLayer(Phases.Player)

        draw.color = Colors.player
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
        val wizLight = GLight(space.centerX, space.centerY, 0.15f)
        val bdfLight = GLight(space.centerX, space.centerY, 0.0f)
        val mainLight = GLight(space.x + 1f, space.centerY + 6f, 0.2f)
        draw.preDraw = {
            if (!GTime.enemyPhase) {
                draw.angle = angleVector.set(space.centerX - GHelper.x, space.centerY - GHelper.y).angle() + 90f
                angleVector.nor()
                wizLight.updatePos(space.centerX, space.centerY)
                mainLight.updateTilt(2f + tiltRandomness.value)
                angleRandomness.tick(GTime.playerDelta)
                intensityRandomness.tick(GTime.playerDelta)
                if (GKeyGlobalState.justTouched) {
                    currentAnim = shootAnim
                }
                draw.currentImg = currentAnim.update(GTime.playerDelta)
                bdfLight.updateIntesity(0f)
            }
            mainLight.updatePosAngle(space.centerX - angleVector.x * 20f, space.centerY - angleVector.y * 20f, draw.angle + angleRandomness.value - 90f)
            mainLight.updateIntesity(0.2f + intensityRandomness.value)
            if (!GKeyGlobalState.touched) {
                currentAnim = defaultAnim
            }
        }
    }

    val shootOffset = Vector2(0f, 18f)
    val angleCompute = Vector2()
    fun getShootOffsetFromCenter(targetX: Float, targetY: Float, space: CompSpace): Vector2 {
        val angle = angleCompute.set(targetX - space.centerX, targetY - space.centerY).angleRad()
        return if (shootLeft)
            shootOffset.setAngleRad(angle - .22f)
        else
            shootOffset.setAngleRad(angle + .22f)
    }

}