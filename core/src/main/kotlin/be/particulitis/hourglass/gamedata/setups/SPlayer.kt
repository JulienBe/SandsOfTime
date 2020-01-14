package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Data
import be.particulitis.hourglass.gamedata.Dim
import be.particulitis.hourglass.gamedata.Layers
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.World
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

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
        player.space().setDim(Dim.Player.w, Dim.Player.w)
        player.space().setPos(GResolution.areaHDim - Dim.Player.half, GResolution.areaHDim - Dim.Player.half)
        shoot.setOffset(Dim.Player.half - Dim.Bullet.half, Dim.Player.half - Dim.Bullet.half)
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
        light.setLight(Colors.player, space.x + 1f, space.centerY - 1f, 0.2f)
        draw.color = Colors.player
        draw.layer = playerLayer
        val trs = GGraphics.tr("link_shoot")
        val nrs = GGraphics.nor("link_shoot")
        draw.drawFront = { batch ->
            light.updatePos(space.x + 2f, space.centerY + 1f)
            batch.draw(trs, space.x, space.y)
        }
        draw.drawNormal = { batch ->
            batch.draw(nrs, space.x, space.y)
        }
    }
}