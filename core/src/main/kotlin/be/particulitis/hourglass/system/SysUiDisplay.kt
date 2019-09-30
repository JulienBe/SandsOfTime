package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.CompTxt
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.annotations.Wire
import com.artemis.systems.IteratingSystem

@Wire(failOnNull = false)
class SysUiDisplay : IteratingSystem(Aspect.all(CompSpace::class.java, CompTxt::class.java)) {
    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mTxt: ComponentMapper<CompTxt>

    override fun process(entityId: Int) {
        val space = mSpace[entityId]
        val txt = mTxt[entityId]
        txt.pixels.forEach {
            it.act(GTime.delta)
            it.draw(space)
        }
    }

}