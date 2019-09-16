package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.CompDir
import be.particulitis.hourglass.comp.CompIsPlayer
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem
import com.badlogic.gdx.Gdx

class SysDirMovement : IteratingSystem(Aspect.all(CompDir::class.java, CompDir::class.java, CompSpace::class.java, CompIsPlayer::class.java)) {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private lateinit var mDir: ComponentMapper<CompDir>
    private lateinit var mPlayer: ComponentMapper<CompIsPlayer>

    override fun process(entityId: Int) {
        val dir = mDir[entityId]
        val dim = mSpace[entityId]
        val player = mPlayer[entityId]

        dim.move(dir.x, dir.y, if (player.isPlayer) GTime.playerDelta else GTime.enemyDelta)
    }
}