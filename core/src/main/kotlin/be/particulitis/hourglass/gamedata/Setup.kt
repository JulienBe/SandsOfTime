package be.particulitis.hourglass.gamedata

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.gamedata.graphics.Anims33
import be.particulitis.hourglass.gamedata.graphics.Colors
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx

object Setup {

    const val bulletDim = 2f
    const val playerTag = "PLAYER"
    const val playerSpeed = 150f

    const val playerLayer = 10
    const val playerBulletLayer = 9
    const val enemyBulletLayer = 2
    const val enemyLayer = 1

    val dirAnims = mapOf(
            GDir.None to Anims33.SquareNoDir,
            GDir.Right to Anims33.SquareRight,
            GDir.DownRight to Anims33.SquareDownRight,
            GDir.Down to Anims33.SquareDown,
            GDir.DownLeft to Anims33.SquareDownLeft,
            GDir.Left to Anims33.SquareLeft,
            GDir.UpLeft to Anims33.SquareUpLeft,
            GDir.Up to Anims33.SquareUp,
            GDir.UpRight to Anims33.SquareUpRight
    )
    val shootAnims = mapOf(
            GDir.None to Anims33.SquareNoDir,
            GDir.Right to Anims33.ShootFromRight,
            GDir.DownRight to Anims33.ShootFromDownRight,
            GDir.Down to Anims33.ShootFromDown,
            GDir.DownLeft to Anims33.ShootFromDownLeft,
            GDir.Left to Anims33.ShootFromLeft,
            GDir.UpLeft to Anims33.ShootFromUpLeft,
            GDir.Up to Anims33.ShootFromUp,
            GDir.UpRight to Anims33.ShootFromUpRight
    )

    fun score(id: Int, world: World) {
        val space = world.getEntity(id).getComponent(CompSpace::class.java)
        space.setPos(90f, 170f)
    }

    fun player(playerEntityId: Int, world: World) {
        val player = world.getEntity(playerEntityId)
        world.getSystem(TagManager::class.java).register(playerTag, playerEntityId)

        val playerControl = player.getComponent(CompControl::class.java)
        val draw = player.getComponent(CompDraw::class.java)
        val space = player.getComponent(CompSpace::class.java)
        val shoot = player.getComponent(CompShooter::class.java)
        val mvt = player.getComponent(CompCharMovement::class.java)
        var anim = shootAnims[GDir.None]

        playerControl.addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        playerControl.addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        playerControl.addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        playerControl.addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        player.getComponent(CompHp::class.java).setHp(10)
        dim(playerEntityId, world, GResolution.areaHDim - Dim.Player.half, GResolution.areaHDim - Dim.Player.half, Dim.Player.w, Dim.Player.w)

        shoot.setOffset((Dim.Player.w - bulletDim) / 2f, (Dim.Player.w - bulletDim) / 2f)
        shoot.setKey(Input.Keys.SPACE)
        shoot.shouldShood = {
            !shoot.keyCheck || (shoot.keyCheck && Gdx.input.justTouched())
        }
        shoot.shootingFunc = {
            val id = world.create(shoot.bullet.first.build(world))
            shoot.dir.set(GHelper.x - space.x, GHelper.y - space.y)
            shoot.dir.nor()
            anim = shootAnims[GDir.get(shoot.dir)]
            shoot.bullet.second.invoke(id, world,
                    space.x + shoot.offsetX + shoot.dir.x / 100f, space.y + shoot.offsetY + shoot.dir.y / 100f,
                    shoot.dir)
            draw.cpt = 0
        }
        shoot.setBullet(Builder.bullet, Setup::playerBullet)
        shoot.setFirerate(.15f)

        val collide = player.getComponent(CompCollide::class.java)
        collide.setIds(Ids.player)
        collide.addCollidingWith(Ids.enemy, Ids.enemyBullet)

        player.getComponent(CompCharMovement::class.java).speed = playerSpeed
        player.getComponent(CompIsPlayer::class.java).setPlayer(true)

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

    fun enemyShoot(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float) {
        val enemy = baseEnemy(id, world, exclusionStartX, exclusionStopX, exclusionStartY, exclusionStopY)
        val space = enemy.getComponent(CompSpace::class.java)
        val shoot = enemy.getComponent(CompShooter::class.java)
        val draw = enemy.getComponent(CompDraw::class.java)
        draw.color = Colors.enemyShoots
        draw.drawingStyle = {batch ->
            DrawMethods.draw33animLoop(space, draw, Anims33.SquareNoDir, 2, Dim.Enemy, batch)
            draw.cpt = (GTime.enemyTime * 10f).toInt()
        }

        shoot.setOffset(Dim.Enemy.half - (bulletDim / 2f), Dim.Enemy.half - (bulletDim / 2f))
        shoot.shouldShood = { true }
        shoot.setBullet(Builder.bullet, Setup::enemyBullet)
        shoot.shootingFunc = {
            val bulletId = world.create(shoot.bullet.first.build(world))
            val playerSpace = world.getSystem(TagManager::class.java).getEntity(playerTag).getComponent(CompSpace::class.java)
            shoot.dir.set(playerSpace.centerX - (space.x + Dim.Enemy.half), playerSpace.centerY - (space.y + Dim.Enemy.half))
            shoot.dir.nor()
            shoot.bullet.second.invoke(bulletId, world,
                    space.x + shoot.offsetX + shoot.dir.x / 100f, space.y + shoot.offsetY + shoot.dir.y / 100f,
                    shoot.dir)
        }
        shoot.setFirerate(2f)
    }

    fun enemySlug(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float) {
        val enemy = baseEnemy(id, world, exclusionStartX, exclusionStopX, exclusionStartY, exclusionStopY)
        val player = world.getSystem(TagManager::class.java).getEntity(playerTag)
        val space = enemy.getComponent(CompSpace::class.java)
        val draw = enemy.getComponent(CompDraw::class.java)
        val dir = enemy.getComponent(CompDir::class.java)

        enemy.getComponent(CompTargetSeek::class.java).target.set(GRand.nextFloat() * 100f, GRand.nextFloat() * 100f)
        enemy.getComponent(CompTargetFollow::class.java).set(player.getComponent(CompSpace::class.java))
        enemy.getComponent(CompDir::class.java).setSpeedAcceleration(20f, 0.3f)
        draw.drawingStyle = { batch ->
            DrawMethods.basic(space, draw, batch)
            DrawMethods.drawTrail(draw, space, dir, batch)
        }
    }

    private fun baseEnemy(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float): Entity {
        dim(id, world, GRand.floatExcludingPlease(0f, GResolution.areaDim - Dim.Enemy.w, exclusionStartX, exclusionStopX), GRand.floatExcludingPlease(0f, GResolution.areaDim - Dim.Enemy.w, exclusionStartY, exclusionStopY), Dim.Enemy.w, Dim.Enemy.w)
        val enemy = world.getEntity(id)
        val collide = enemy.getComponent(CompCollide::class.java)
        collide.setIds(Ids.enemy)
        collide.addCollidingWith(Ids.player, Ids.playerBullet)
        enemy.getComponent(CompIsPlayer::class.java).setPlayer(false)
        enemy.getComponent(CompDraw::class.java).color = Colors.enemy
        enemy.getComponent(CompDraw::class.java).layer = enemyLayer
        return enemy
    }

    fun enemyBullet(id: Int, world: World, posX: Float, posY: Float, dir: Vector2) {
        dim(id, world, posX, posY, bulletDim, bulletDim)
        dir.scl(22f)
        val bullet = world.getEntity(id)
        val space = bullet.getComponent(CompSpace::class.java)
        val draw = bullet.getComponent(CompDraw::class.java)
        val collide = bullet.getComponent(CompCollide::class.java)

        bullet.getComponent(CompDir::class.java).set(dir)
        bullet.getComponent(CompDir::class.java).setSpeedAcceleration(100f, 100f)
        bullet.getComponent(CompIsPlayer::class.java).setPlayer(false)
        bullet.getComponent(CompHp::class.java).setHp(1)
        collide.setIds(Ids.enemyBullet)
        collide.addCollidingWith(Ids.player)
        bullet.getComponent(CompTtl::class.java).remaining = 9f
        draw.color = Colors.enemyBullets
        draw.drawingStyle = {batch -> DrawMethods.basic(space, draw, batch)}
        draw.layer = enemyBulletLayer
    }

    fun playerBullet(id: Int, world: World, posX: Float, posY: Float, dir: Vector2) {
        dim(id, world, posX, posY, bulletDim, bulletDim)
        dir.scl(160f)
        val bullet = world.getEntity(id)
        val collide = bullet.getComponent(CompCollide::class.java)
        val space = bullet.getComponent(CompSpace::class.java)
        val draw = bullet.getComponent(CompDraw::class.java)
        bullet.getComponent(CompDir::class.java).set(dir)
        bullet.getComponent(CompIsPlayer::class.java).setPlayer(true)
        bullet.getComponent(CompHp::class.java).setHp(100000)
        collide.setIds(Ids.playerBullet)
        collide.addCollidingWith(Ids.enemy)
        bullet.getComponent(CompTtl::class.java).remaining = 1f
        draw.color = Colors.playerBullets
        draw.drawingStyle = {batch -> DrawMethods.basic(space, draw, batch)}
        draw.layer = playerBulletLayer
    }

    private fun dim(id: Int, world: World, x: Float, y: Float, w: Float, h: Float) {
        val dim = world.getEntity(id).getComponent(CompSpace::class.java)
        dim.setDim(w, h)
        dim.setPos(x, y)
    }
}