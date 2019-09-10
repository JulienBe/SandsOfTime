package be.particulitis.hourglass.comp

import com.artemis.Component

class CompHp : Comp() {

    var hp = 1
        private set

    fun setHp(hp: Int) {
        this.hp = hp
    }

    fun addHp(hp: Int) {
        this.hp += hp
    }

    override fun reset() {
        super.reset()
        hp = 1
    }
}