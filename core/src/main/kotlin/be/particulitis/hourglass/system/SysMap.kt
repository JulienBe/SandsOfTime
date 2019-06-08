package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.common.GResolution
import be.particulitis.hourglass.comp.CompPos
import com.artemis.Aspect
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color

// checking that ratio is properly dealt with. Just replace CompPos by something when tiles are added
class SysMap : IteratingSystem(Aspect.all(CompPos::class.java)) {

    override fun begin() {
        super.begin()
        FirstScreen.batch.draw(mapColor, 0f, 0f, GResolution.areaDim, GResolution.areaDim)
    }

    override fun process(entityId: Int) {
    }

    companion object {
        val mapColor = Color(0.1f, 0.1f, 0.1f, 1f)
    }
}