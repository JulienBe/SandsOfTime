package be.particulitis.hourglass

import be.particulitis.hourglass.gamedata.Aspects
import be.particulitis.hourglass.common.GSounds
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.comp.CompTimePhase
import com.artemis.Aspect
import com.artemis.EntitySubscription
import com.artemis.EntitySubscription.SubscriptionListener
import com.artemis.World
import com.artemis.utils.IntBag
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import ktx.collections.gdxArrayOf

class Boombox(world: World) {

    private val explosionSlug = world.aspectSubscriptionManager.get(Aspect.all(Aspects.EnemySlug.comps))
    private val explosionShooter = world.aspectSubscriptionManager.get(Aspect.all(Aspects.EnemyShoot.comps))
    private val shoot = world.aspectSubscriptionManager.get(Aspect.all(Aspects.Bullet.comps))
    val mTitleScreen = GGraphics.assMan.music(AssMan.titleScreen)
    val mLevel1 = GGraphics.assMan.music(AssMan.level1)
    val mLevel2 = GGraphics.assMan.music(AssMan.level2)
    val mLevel3 = GGraphics.assMan.music(AssMan.level3)
    val mLevelEnd = GGraphics.assMan.music(AssMan.ending)
    val musics = gdxArrayOf(mTitleScreen, mLevel1, mLevel2, mLevel3, mLevelEnd)
    val playerMapper = world.getMapper(CompTimePhase::class.java)

    init {
        onRemove(explosionShooter, GSounds.explosion3)
        onRemove(explosionSlug, GSounds.explosion2)
        shoot.addSubscriptionListener(object : SubscriptionListener{
            override fun inserted(entities: IntBag) {
                for (i in 0 until entities.size()) {
                    if (playerMapper[entities[i]].isPlayer)
                        GSounds.shot.play()
                }
            }
            override fun removed(entities: IntBag) { }
        })
    }

    private fun onRemove(subscription: EntitySubscription, sound: Sound) {
        subscription.addSubscriptionListener(object : SubscriptionListener {
            override fun removed(entities: IntBag) { sound.play() }
            override fun inserted(entities: IntBag) { }
        })
    }

    fun play(titleScreen: Music) {
        titleScreen.play()
        titleScreen.volume = 0.5f
        titleScreen.isLooping = true
    }

    fun stopAll() {
        musics.filter { it.isPlaying }.forEach { it.stop() }
    }

}