package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompLight
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysLightTrack : IteratingSystem(Aspect.all(CompLight::class.java, CompSpace::class.java)) {

    private lateinit var mLight: ComponentMapper<CompLight>
    private lateinit var mSpace: ComponentMapper<CompSpace>

    override fun process(entityId: Int) {
        val light = mLight[entityId]
        val space = mSpace[entityId]

        light.updatePos(space.centerX, space.centerY)
    }
}