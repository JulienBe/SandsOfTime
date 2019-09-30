package be.particulitis.hourglass.system

import be.particulitis.hourglass.FirstScreen
import be.particulitis.hourglass.builds.Aspects
import be.particulitis.hourglass.builds.Builder
import be.particulitis.hourglass.comp.CompCollide
import be.particulitis.hourglass.comp.CompScore
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.comp.CompTxt
import be.particulitis.hourglass.forEach
import com.artemis.*
import com.artemis.EntitySubscription.SubscriptionListener
import com.artemis.utils.IntBag

class SysScore : BaseEntitySystem(Aspect.all(CompScore::class.java)) {

    var previousScore = 0
    private lateinit var mScore: ComponentMapper<CompScore>
    private lateinit var mTxt: ComponentMapper<CompTxt>

    override fun processSystem() {
        if (previousScore != FirstScreen.score)
            updateComp()
        previousScore = FirstScreen.score
    }

    private fun updateComp() {
        val actives = subscription.entities
        val ids: IntArray = actives.data
        for (it in actives.size() - 1 downTo 0) {
            val id = ids[it]
            if (mTxt.has(id)) {
                val score = mScore[id]
                mTxt.get(id).set(FirstScreen.score.toString())
            }
        }
    }
}