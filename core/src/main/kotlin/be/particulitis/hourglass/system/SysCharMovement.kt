package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GAction
import be.particulitis.hourglass.comp.CompAction
import be.particulitis.hourglass.comp.CompCharMovement
import be.particulitis.hourglass.comp.CompPos
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysCharMovement : IteratingSystem(Aspect.all(CompAction::class.java, CompCharMovement::class.java, CompPos::class.java)) {

    private lateinit var mAction: ComponentMapper<CompAction>
    private lateinit var mPos: ComponentMapper<CompPos>
    private lateinit var mMvt: ComponentMapper<CompCharMovement>

    override fun process(entityId: Int) {
        val action = mAction[entityId]
        val pos = mPos[entityId]
        val mvt = mMvt[entityId]
        action.actions.onEach { println(it) }.forEach {
            when (it) {
                GAction.LEFT -> pos.pos.x -= mvt.speed
                GAction.RIGHT -> pos.pos.x += mvt.speed
                GAction.UP -> pos.pos.y += mvt.speed
                GAction.DOWN -> pos.pos.y -= mvt.speed
             }
        }
    }
}