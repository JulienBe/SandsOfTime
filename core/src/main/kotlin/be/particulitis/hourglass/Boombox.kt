package be.particulitis.hourglass

import be.particulitis.hourglass.builds.Aspects
import be.particulitis.hourglass.common.GSounds
import be.particulitis.hourglass.comp.CompIsPlayer
import com.artemis.Aspect
import com.artemis.EntitySubscription
import com.artemis.EntitySubscription.SubscriptionListener
import com.artemis.World
import com.artemis.utils.IntBag
import com.badlogic.gdx.audio.Sound

class Boombox(world: World) {

    val explosionSlug = world.aspectSubscriptionManager.get(Aspect.all(Aspects.EnemySlug.comps))
    val explosionShooter = world.aspectSubscriptionManager.get(Aspect.all(Aspects.EnemyShoot.comps))
    val shoot = world.aspectSubscriptionManager.get(Aspect.all(Aspects.Bullet.comps))
    val playerMapper = world.getMapper(CompIsPlayer::class.java)

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

}