package be.particulitis.hourglass.builds

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.comp.*
import com.artemis.Entity
import com.artemis.World
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.artemis.managers.TagManager
import com.badlogic.gdx.Gdx

object Setup {

    const val bulletDim = 2f
    const val enemyDim = 10f
    const val playerDim = 5f
    const val playerHDim = playerDim / 2f
    // tag manager is null for some reason, waiting on a response https://gitter.im/junkdog/artemis-odb. We'll see
    const val playerTag = "PLAYER"
    const val playerSpeed = 150f

    const val playerLayer = 10
    const val playerBulletLayer = 9
    const val enemyBulletLayer = 2
    const val enemyLayer = 1

    fun score(id: Int, world: World) {
        val space = world.getEntity(id).getComponent(CompSpace::class.java)
        space.setPos(90f, 170f)
    }

    fun player(playerEntityId: Int, world: World) {
        val player = world.getEntity(playerEntityId)
        world.getSystem(TagManager::class.java).register(playerTag, playerEntityId)

        val playerControl = player.getComponent(CompControl::class.java)
        val draw = player.getComponent(CompDraw::class.java)
        playerControl.addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        playerControl.addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        playerControl.addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        playerControl.addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        player.getComponent(CompHp::class.java).setHp(10)
        dim(playerEntityId, world, GResolution.areaHDim - playerHDim, GResolution.areaHDim - playerHDim, playerDim, playerDim)

        val space = player.getComponent(CompSpace::class.java)
        val shoot = player.getComponent(CompShooter::class.java)
        shoot.setOffset((playerDim - bulletDim) / 2f, (playerDim - bulletDim) / 2f)
        shoot.setKey(Input.Keys.SPACE)
        shoot.shouldShood = {
            !shoot.keyCheck || (shoot.keyCheck && Gdx.input.justTouched())
        }
        shoot.shootingFunc = {
            val id = world.create(shoot.bullet.first.build(world))
            shoot.dir.set(GHelper.x - space.x, GHelper.y - space.y)
            shoot.dir.nor()
            shoot.bullet.second.invoke(id, world,
                    space.x + shoot.offsetX + shoot.dir.x / 100f, space.y + shoot.offsetY + shoot.dir.y / 100f,
                    shoot.dir)
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
        draw.drawingStyle = {batch -> DrawMethods.basic(space, draw, batch)}
    }

    fun enemyShoot(id: Int, world: World, exclusionStartX: Float, exclusionStopX: Float, exclusionStartY: Float, exclusionStopY: Float) {
        val enemy = baseEnemy(id, world, exclusionStartX, exclusionStopX, exclusionStartY, exclusionStopY)
        val space = enemy.getComponent(CompSpace::class.java)
        val shoot = enemy.getComponent(CompShooter::class.java)
        val draw = enemy.getComponent(CompDraw::class.java)
        draw.color = Colors.enemyShoots
        draw.drawingStyle = {batch -> DrawMethods.basic(space, draw, batch)}

        shoot.setOffset((enemyDim - bulletDim) / 2f, (enemyDim - bulletDim) / 2f)
        shoot.shouldShood = { true }
        shoot.setBullet(Builder.bullet, Setup::enemyBullet)
        shoot.shootingFunc = {
            val id = world.create(shoot.bullet.first.build(world))
            val playerSpace = world.getSystem(TagManager::class.java).getEntity(playerTag).getComponent(CompSpace::class.java)
            shoot.dir.set(playerSpace.centerX - (space.x + enemyDim / 2f), playerSpace.centerY - (space.y + enemyDim / 2f))
            shoot.dir.nor()
            shoot.bullet.second.invoke(id, world,
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
        dim(id, world, GRand.floatExcludingPlease(0f, GResolution.areaDim - enemyDim, exclusionStartX, exclusionStopX), GRand.floatExcludingPlease(0f, GResolution.areaDim - enemyDim, exclusionStartY, exclusionStopY), enemyDim, enemyDim)
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