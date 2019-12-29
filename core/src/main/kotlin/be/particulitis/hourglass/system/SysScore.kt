package be.particulitis.hourglass.system

import be.particulitis.hourglass.comp.CompScore
import be.particulitis.hourglass.comp.ui.CompTxt
import be.particulitis.hourglass.screens.FirstScreen
import com.artemis.Aspect
import com.artemis.BaseEntitySystem
import com.artemis.ComponentMapper

class SysScore : BaseEntitySystem(Aspect.all(CompScore::class.java)) {

    var previousScore = 0
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
                mTxt.get(id).set(FirstScreen.score.toString())
            }
        }
    }
}