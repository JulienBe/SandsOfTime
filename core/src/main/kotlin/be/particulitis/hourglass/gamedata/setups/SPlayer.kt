package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.*
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

    fun player(playerEntityId: Int, world: World) {
        val player = world.getEntity(playerEntityId)
        world.getSystem(TagManager::class.java).register(Data.playerTag, playerEntityId)

        val draw = player.draw()
        val space = player.space()
        val shoot = player.shooter()
        var anim = Data.shootAnims[GDir.None]

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
            val id = world.create(shoot.bullet.first.build(world))
            shoot.dir.set(GHelper.x - space.x, GHelper.y - space.y)
            shoot.dir.nor()
            anim = Data.shootAnims[GDir.get(shoot.dir)]
            shoot.bullet.second.invoke(id, world,
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

        player.light().setLight(Colors.player, space.centerX, space.centerY, 1.8f)
        draw.color = Colors.player
        draw.layer = playerLayer
        draw.drawingStyle = {batch ->
            DrawMethods.draw33animNoLoop(space, draw, anim!!, 2, Dim.Player, batch)
            draw.accu += GTime.playerDelta * 10f
            if (draw.accu >= 1f) {
                draw.cpt++
                draw.accu -= 1f
            }
        }
    }
}