package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.CompDir
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx

class SysDirMovement : IteratingSystem(Aspect.all(CompDir::class.java, CompDir::class.java, CompSpace::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mDir: ComponentMapper<CompDir>

    override fun process(entityId: Int) {
        val dir = mDir[entityId]
        val dim = mSpace[entityId]

        dim.move(dir.x * Gdx.graphics.deltaTime, dir.y * Gdx.graphics.deltaTime)
    }
}