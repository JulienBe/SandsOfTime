package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompDir
import be.particulitis.hourglass.comp.CompHp
import be.particulitis.hourglass.comp.CompSeekTarget
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2

class SysTargetSeek : IteratingSystem(Aspect.all(CompSeekTarget::class.java)) {

    private lateinit var mTarget: ComponentMapper<CompSeekTarget>
    private lateinit var mDir: ComponentMapper<CompDir>
    private lateinit var mSpace: ComponentMapper<CompSpace>

    override fun process(entityId: Int) {
        val target = mTarget[entityId]
        val dir = mDir[entityId]
        val space = mSpace[entityId]

        val xToAdd = target.x - space.x
        val yToAdd = target.y - space.y
        val dirToAdd = Vector2(xToAdd, yToAdd).clamp(-dir.maxAcceleration, dir.maxAcceleration)
        dir.add(dirToAdd.x, dirToAdd.y)
        dir.clamp()
    }
}