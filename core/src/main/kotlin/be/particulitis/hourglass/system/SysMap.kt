package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.builds.Colors
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.systems.IteratingSystem

// checking that ratio is properly dealt with. Just replace CompSpace by something when tiles are added
class SysMap : IteratingSystem(Aspect.all(CompSpace::class.java)) {

    override fun begin() {
        super.begin()
        FirstScreen.batch.draw(Colors.map, 0f, 0f, GResolution.areaDim)
    }

    override fun process(entityId: Int) {
    }

}