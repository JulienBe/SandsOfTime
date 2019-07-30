package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.*
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx

class SysShooter : IteratingSystem(Aspect.all(CompShooter::class.java, CompSpace::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mShoot: ComponentMapper<CompShooter>

    override fun process(entityId: Int) {
        val shoot = mShoot[entityId]
        val dim = mSpace[entityId]

        if (shoot.nextShoot < System.currentTimeMillis() &&
                (!shoot.keyCheck || (shoot.keyCheck && Gdx.input.isKeyPressed(shoot.keyToCheck)))) {
            val id = world.create(shoot.bullet.first.build(world))
            shoot.bullet.second.invoke(id, world, dim.x + shoot.offsetX, dim.y + shoot.offsetY, shoot.dir)
        }
    }
}