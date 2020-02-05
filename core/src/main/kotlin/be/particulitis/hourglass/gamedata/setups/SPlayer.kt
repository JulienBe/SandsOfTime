package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.common.drawing.GGraphics
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
import kotlin.math.min
import kotlin.math.roundToInt

object SPlayer : Setup() {

    private const val playerSpeed = 150f
    private const val playerLayer = 10
    private const val maxCharge = 0.5f

    fun player(world: World, offsetX: Float = 0f, offsetY: Float = 0f) {
        val player = world.create(Builder.player)
        world.getSystem(TagManager::class.java).register(Data.playerTag, player.id)

        val draw = player.draw()
        val space = player.space()
        val shoot = player.shooter()
        var chargeValue = 0f
        var charge = false

        player.control().addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        player.control().addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        player.control().addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        player.control().addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        player.hp().setHp(100)
        player.space().setDim(Dim.Player)
        player.space().setPos(offsetX + GResolution.areaHW - Dim.Player.hw, offsetY + GResolution.areaHH - Dim.Player.hw)
        val shootAnim = GGraphics.anim(ImgMan.animPlayerShoot, 0.04f)
        val defaultAnim = GGraphics.anim("wizard_f", .3f)
        var currentAnim = defaultAnim
        shootAnim.finish()
        shoot.setFirerate(0.5f)
        shoot.setOffset(Dim.PlayerSprite.hw - Dim.Bullet.hw, Dim.PlayerSprite.hh - Dim.Bullet.hw)
        shoot.setKey(Input.Keys.SPACE)
        shoot.shouldShood = {
            !shoot.keyCheck || (shoot.keyCheck && GKeyGlobalState.justReleased)
        }

        shoot.shootingFunc = {
            currentAnim = defaultAnim.start()
            val position = getShootPosition(GHelper.x, GHelper.y, space)
            shoot.dir.set(GHelper.x - position.x, GHelper.y - position.y)
            shoot.dir.nor()
            val bulletEntity = shoot.bullet.second.invoke(world, position.x, position.y, shoot.dir, 1 + (min(chargeValue, maxCharge) * 10).roundToInt())

            chargeValue = 0f
            charge = false
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
        val light = player.light()
        val wizLight = light.setLight(Colors.player, space.centerX, space.centerY, 0.15f)
        val bdfLight = light.setLight(Colors.player, space.centerX, space.centerY, 0.0f)
        val mainLight = light.setLight(Colors.player, space.x + 1f, space.centerY + 6f, 0.2f)
        draw.preDraw = {
            if (!GTime.enemyPhase) {
                draw.angle = angleVector.set(space.centerX - GHelper.x, space.centerY - GHelper.y).angle() + 90f
                angleVector.nor()
                light.updatePos(space.centerX, space.centerY, wizLight)
                light.updateTilt(2f + tiltRandomness.value, mainLight)
                angleRandomness.tick(GTime.playerDelta)
                intensityRandomness.tick(GTime.playerDelta)
                if (GKeyGlobalState.justTouched) {
                    currentAnim = shootAnim.start()
                    charge = true
                }
                draw.currentImg = currentAnim.frame(GTime.playerDelta)
                light.updateIntesity(0f, bdfLight)
                if (charge) {
                    chargeValue += GTime.playerDelta
                    val position = getShootPosition(GHelper.x, GHelper.y, space)
                    for (i in 0 until (chargeValue * 500 * GTime.playerDelta).roundToInt())
                        SParticles.chargingParticles(world, position.x, position.y, chargeValue)
                    light.updateIntesity(min(maxCharge, chargeValue), bdfLight)
                    light.updatePos(position.x, position.y, bdfLight)
                    if (chargeValue > maxCharge) {
                        light.updateIntesity(maxCharge + GRand.gauss(.05f), bdfLight)
                    }
                }
            }
            light.updatePosAngle(space.centerX - angleVector.x * 20f, space.centerY - angleVector.y * 20f, draw.angle + angleRandomness.value - 90f, mainLight)
            light.updateIntesity(0.2f + intensityRandomness.value, mainLight)
            if (!GKeyGlobalState.touched) {
                currentAnim = defaultAnim
                charge = false
            }
        }
    }

    val shootOffsetVector = Vector2(Dim.PlayerSprite.hw, Dim.PlayerSprite.hh)
    val shootStart = Vector2()
    val shootTarget = Vector2()
    fun getShootPosition(targetX: Float, targetY: Float, space: CompSpace): Vector2 {
        shootTarget.set(targetX - space.centerX, targetY - space.centerY)
        shootOffsetVector.setAngle(shootTarget.angle())
        shootStart.set(
                space.x + shootOffsetVector.x,
                space.y + shootOffsetVector.y
        )
        return shootStart
    }

}