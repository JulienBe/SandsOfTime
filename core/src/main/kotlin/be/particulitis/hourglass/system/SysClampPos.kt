package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.comp.CompDimension
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils

class SysClampPos : IteratingSystem(Aspect.all(CompDimension::class.java)) {

    private lateinit var mDimension: ComponentMapper<CompDimension>

    override fun process(entityId: Int) {
        val dim = mDimension[entityId]
        dim.pos.set(MathUtils.clamp(dim.x, 0f, GResolution.areaDim - dim.w), MathUtils.clamp(dim.y, 0f, GResolution.areaDim - dim.h))
    }
}