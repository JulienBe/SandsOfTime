package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompIsPlayer
import be.particulitis.hourglass.comp.CompShooter
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx

class SysShooter : IteratingSystem(Aspect.all(CompShooter::class.java, CompSpace::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mShoot: ComponentMapper<CompShooter>
    private lateinit var mIsPlayer: ComponentMapper<CompIsPlayer>

    override fun process(entityId: Int) {
        val shoot = mShoot[entityId]
        val space = mSpace[entityId]

        if (shoot.nextShoot < GTime.myTime(mIsPlayer.has(entityId)) &&
                (!shoot.keyCheck || (shoot.keyCheck && Gdx.input.justTouched()))) {
            val id = world.create(shoot.bullet.first.build(world))
            val shootDir = shoot.dir.invoke(space.centerX, space.centerY).scl(150f)
            shoot.bullet.second.invoke(id, world,
                    space.x + shoot.offsetX + shootDir.x / 100f, space.y + shoot.offsetY + shootDir.y / 100f,
                    shootDir)
            shoot.nextShoot = GTime.myTime(mIsPlayer.has(entityId)) + shoot.firerate
        }
    }
}