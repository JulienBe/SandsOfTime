package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompCollide
import be.particulitis.hourglass.comp.CompHp
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper
import com.badlogic.gdx.Gdx

class SysDamage : BaseEntitySystem(Aspect.all(CompCollide::class.java, CompHp::class.java)) {

    private lateinit var mCollide: ComponentMapper<CompCollide>
    private lateinit var mHp: ComponentMapper<CompHp>

    override fun processSystem() {
        val actives = subscription.entities
        val ids: IntArray = actives.data
        for (it in actives.size() - 1 downTo 0) {
            val col = mCollide[ids[it]]
            if (col.dmgToTake > 0 && col.dmgTakenTime < System.currentTimeMillis()) {
                val hp = mHp[ids[it]]
                hp.addHp(-col.dmgToTake)
                col.setDmgTakenTime(System.currentTimeMillis() + 100L)
            }
            col.setDmgToTake(0)
        }
    }

}