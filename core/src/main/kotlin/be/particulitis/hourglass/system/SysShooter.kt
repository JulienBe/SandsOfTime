package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.comp.CompIsPlayer
import be.particulitis.hourglass.comp.CompShooter
import be.particulitis.hourglass.comp.CompSpace
import com.artemis.Aspect
import com.artemis.ComponentMapper
import com.artemis.systems.IteratingSystem

class SysShooter : IteratingSystem(Aspect.all(CompShooter::class.java, CompSpace::class.java)) {

    private lateinit var mShoot: ComponentMapper<CompShooter>
    private lateinit var mIsPlayer: ComponentMapper<CompIsPlayer>

    override fun process(entityId: Int) {
        val shoot = mShoot[entityId]
        shoot.justShot = false
        val player = mIsPlayer[entityId].isPlayer
        if ((GTime.enemyPhase && !player || !GTime.enemyPhase && player) &&
                shoot.nextShoot < GTime.myTime(player) &&
                shoot.shouldShood.invoke()) {
            shoot.shootingFunc.invoke()
            shoot.nextShoot = GTime.myTime(mIsPlayer.has(entityId)) + shoot.firerate
            shoot.justShot = true
        }
    }
}