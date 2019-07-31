package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompHp
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx

class SysDead : IteratingSystem(Aspect.all(CompHp::class.java)) {

    private lateinit var mHp: ComponentMapper<CompHp>

    override fun process(entityId: Int) {
        val hp = mHp[entityId]

        if (hp.hp <= 0) {
            println("${Gdx.graphics.frameId}: delete $entityId with hp ${hp.hp} ")
            world.delete(entityId)
        }
    }
}