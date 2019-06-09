package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GAction
import be.particulitis.hourglass.comp.CompAction
import be.particulitis.hourglass.comp.CompCharMovement
import be.particulitis.hourglass.comp.CompDimension
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysCharMovement : IteratingSystem(Aspect.all(CompAction::class.java, CompCharMovement::class.java, CompDimension::class.java)) {

    private lateinit var mAction: ComponentMapper<CompAction>
    private lateinit var mDimension: ComponentMapper<CompDimension>
    private lateinit var mMvt: ComponentMapper<CompCharMovement>

    override fun process(entityId: Int) {
        val action = mAction[entityId]
        val pos = mDimension[entityId].pos
        val mvt = mMvt[entityId]

        mvt.vec.set(0f, 0f)
        action.actions.forEach {
            when (it) {
                GAction.LEFT    -> mvt.vec.x -= 1f
                GAction.RIGHT   -> mvt.vec.x += 1f
                GAction.UP      -> mvt.vec.y += 1f
                GAction.DOWN    -> mvt.vec.y -= 1f
             }
        }
        mvt.vec.nor().scl(mvt.speed)
        pos.set(pos.x + mvt.vec.x, pos.y + mvt.vec.y)
    }
}