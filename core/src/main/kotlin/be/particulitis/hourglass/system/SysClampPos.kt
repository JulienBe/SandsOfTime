package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils

class SysClampPos : IteratingSystem(Aspect.all(CompSpace::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>

    override fun process(entityId: Int) {
        val dim = mSpace[entityId]
        dim.setPos(MathUtils.clamp(dim.x, 0f, GResolution.areaDim - dim.w), MathUtils.clamp(dim.y, 0f, GResolution.areaDim - dim.h))
    }
}