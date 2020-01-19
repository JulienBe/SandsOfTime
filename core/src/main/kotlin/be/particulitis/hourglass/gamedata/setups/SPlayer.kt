package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.ImgMan
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.Layers
import be.particulitis.hourglass.gamedata.graphics.Colors
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2

object SPlayer : Setup() {

    private const val playerSpeed = 150f
    private const val playerLayer = 10

    fun player(world: World) {
        val player = world.create(Builder.player)
        world.getSystem(TagManager::class.java).register(Data.playerTag, player.id)

        val draw = player.draw()
        val space = player.space()
        val shoot = player.shooter()

        player.control().addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        player.control().addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        player.control().addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        player.control().addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        player.hp().setHp(10)
        player.space().setDim(Dim.Player)
        player.space().setPos(GResolution.areaHDim - Dim.Player.hw, GResolution.areaHDim - Dim.Player.hw)
        shoot.setOffset(Dim.Player.hw - Dim.Bullet.hw, Dim.Player.hw - Dim.Bullet.hw)
        shoot.setKey(Input.Keys.SPACE)
        shoot.shouldShood = {
            !shoot.keyCheck || (shoot.keyCheck && Gdx.input.justTouched())
        }
        shoot.shootingFunc = {
            shoot.dir.set(GHelper.x - space.x, GHelper.y - space.y)
            shoot.dir.nor()
            shoot.bullet.second.invoke(world,
                    space.x + shoot.offsetX + shoot.dir.x / 100f, space.y + shoot.offsetY + shoot.dir.y / 100f,
                    shoot.dir)
            draw.cpt = 0
        }
        shoot.setBullet(Builder.bullet, SBullet::playerBullet)
        shoot.setFirerate(.15f)

        player.collide().setIds(Ids.player)
        player.collide().addCollidingWith(Ids.enemy, Ids.enemyBullet)

        player.charMvt().speed = playerSpeed
        player.layer().setLayer(Layers.Player)

        val light = player.light()
        light.setLight(Colors.player, space.x + 1f, space.centerY + 6f, 0.1f)
        draw.color = Colors.player
        draw.layer = playerLayer
        val trs = GGraphics.tr(ImgMan.player)
        val nrs = GGraphics.nor(ImgMan.player)
        val angleVector = Vector2()
        val angleRandomness = GPeriodicValue(0.1f) {
            GRand.nextGaussian().toFloat()
        }
        val intensityRandomness = GPeriodicValue(0.11f) {
            GRand.nextGaussian().toFloat() / 1000f
        }
        val tiltRandomness = GPeriodicValue(0.09f) {
            GRand.nextGaussian().toFloat() / 10f
        }
        draw.drawFront = { batch ->
            angleRandomness.tick(GTime.delta)
            intensityRandomness.tick(GTime.delta)
            draw.normalAngle = angleVector.set(space.centerX - GHelper.x, space.centerY - GHelper.y).angle() + 45f
            angleVector.nor()
            light.updatePosAngle((space.x + 6) - angleVector.x * 9, (space.centerY + 10f) - angleVector.y * 9, draw.normalAngle + angleRandomness.value - 45f)
            light.updateIntesity(0.15f + intensityRandomness.value)
            light.updateTilt(4f + tiltRandomness.value)
            batch.draw(trs, space, Dim.PlayerSprite, draw.normalAngle)
        }
        draw.drawNormal = { batch ->
            batch.draw(nrs, space, Dim.PlayerSprite, draw.normalAngle)
        }
    }

}