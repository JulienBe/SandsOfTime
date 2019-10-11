package be.particulitis.hourglass.comp

import be.particulitis.hourglass.Ids

class CompCollide : Comp() {

    var dmgToInflict = 1
        private set
    var dmgToTake = 0
        private set
    var dmgTakenTime = 0L
        private set
    var id = 0
        private set
    var collidesWith = 0
        private set

    fun setDmgToInflict(dmg: Int) {
        this.dmgToInflict = dmg
    }

    fun setDmgToTake(dmg: Int) {
        this.dmgToTake = dmg
    }

    fun setDmgTakenTime(time: Long) {
        dmgTakenTime = time
        dmgToTake = 0
    }

    fun setIds(id: Ids) {
        this.id = id.id
    }

    fun addCollidingWith(vararg ids: Ids) {
        ids.forEach {
            collidesWith = collidesWith or it.id
        }
    }

    fun collidesWith(collide: CompCollide) {
    }

    override fun reset() {
        super.reset()
        id = 0
        collidesWith = 0
        dmgTakenTime = 0L
        dmgToInflict = 1
    }
}