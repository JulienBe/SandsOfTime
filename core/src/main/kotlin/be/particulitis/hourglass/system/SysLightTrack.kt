package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompLights
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysLightTrack : IteratingSystem(Aspect.all(CompLights::class.java, CompSpace::class.java)) {

    private lateinit var mLight: ComponentMapper<CompLights>
    private lateinit var mSpace: ComponentMapper<CompSpace>

    override fun process(entityId: Int) {
        val light = mLight[entityId]
        val space = mSpace[entityId]

        light.updatePos(space.centerX, space.centerY)
    }
}