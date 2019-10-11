package be.particulitis.hourglass.builds

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.*
import be.particulitis.hourglass.comp.*
import com.artemis.World
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.artemis.managers.TagManager

object Setup {

    const val enemyDim = 10f
    const val playerDim = 5f
    const val playerHDim = playerDim / 2f
    // tag manager is null for some reason, waiting on a response https://gitter.im/junkdog/artemis-odb. We'll see
    const val playerTag = "PLAYER"
    const val playerSpeed = 150f

    fun score(id: Int, world: World) {
        val space = world.getEntity(id).getComponent(CompSpace::class.java)
        space.setPos(90f, 170f)
    }

    fun player(playerEntityId: Int, world: World) {
        val player = world.getEntity(playerEntityId)
        world.getSystem(TagManager::class.java).register(playerTag, playerEntityId)

        val playerControl = player.getComponent(CompControl::class.java)
        playerControl.addAction(listOf(Input.Keys.Q, Input.Keys.A,      Input.Keys.LEFT),   GAction.LEFT)
        playerControl.addAction(listOf(Input.Keys.D, Input.Keys.RIGHT),                     GAction.RIGHT)
        playerControl.addAction(listOf(Input.Keys.Z, Input.Keys.W,      Input.Keys.UP),     GAction.UP)
        playerControl.addAction(listOf(Input.Keys.S, Input.Keys.DOWN),                      GAction.DOWN)

        player.getComponent(CompHp::class.java).setHp(10)
        dim(playerEntityId, world, GResolution.areaHDim - playerHDim, GResolution.areaHDim - playerHDim, playerDim, playerDim)

        val shoot = player.getComponent(CompShooter::class.java)
        shoot.setKey(Input.Keys.SPACE)
        shoot.setShootingDir { x, y ->
            shoot.iDir.set(GHelper.x - x, GHelper.y - y)
            shoot.iDir.nor()
        }

        val collide = player.getComponent(CompCollide::class.java)
        collide.setIds(Ids.player)
        collide.addCollidingWith(Ids.enemy)

        player.getComponent(CompCharMovement::class.java).speed = playerSpeed
        player.getComponent(CompIsPlayer::class.java).setPlayer(true)
        player.getComponent(CompDraw::class.java).color = Colors.player
    }

    fun enemy(id: Int, world: World) {
        dim(id, world, GRand.float(0f, GResolution.areaDim - enemyDim), GRand.float(0f, GResolution.areaDim - enemyDim), enemyDim, enemyDim)
        val enemy = world.getEntity(id)
        val collide = enemy.getComponent(CompCollide::class.java)
        collide.setIds(Ids.enemy)
        collide.addCollidingWith(Ids.player, Ids.playerBullet)

        enemy.getComponent(CompTargetSeek::class.java).target.set(GRand.nextFloat() * 100f, GRand.nextFloat() * 100f)
        val player = world.getSystem(TagManager::class.java).getEntity(playerTag)
        enemy.getComponent(CompTargetFollow::class.java).set(player.getComponent(CompSpace::class.java))
        enemy.getComponent(CompIsPlayer::class.java).setPlayer(false)
        enemy.getComponent(CompDir::class.java).setSpeedAcceleration(20f, 3f)
        enemy.getComponent(CompDraw::class.java).color = Colors.enemy
    }

    fun bullet(id: Int, world: World, posX: Float, posY: Float, dir: Vector2) {
        dim(id, world, posX, posY, 1f, 1f)
        val bullet = world.getEntity(id)
        bullet.getComponent(CompDir::class.java).set(dir)
        bullet.getComponent(CompIsPlayer::class.java).setPlayer(true)
        bullet.getComponent(CompHp::class.java).setHp(100000)
        val collide = bullet.getComponent(CompCollide::class.java)
        collide.setIds(Ids.playerBullet)
        collide.addCollidingWith(Ids.enemy)
        bullet.getComponent(CompTtl::class.java).remaining = 1f
        bullet.getComponent(CompDraw::class.java).color = Colors.playerBullets
    }

    private fun dim(id: Int, world: World, x: Float, y: Float, w: Float, h: Float) {
        val dim = world.getEntity(id).getComponent(CompSpace::class.java)
        dim.setDim(w, h)
        dim.setPos(x, y)
    }
}