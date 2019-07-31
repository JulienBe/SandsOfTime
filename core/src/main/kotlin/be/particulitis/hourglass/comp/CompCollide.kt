package be.particulitis.hourglass.comp

class CompCollide : Comp() {

    var dmgToInflict = 1
        private set
    var dmgToTake = 0
        private set
    var dmgTakenTime = 0L
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

    fun collidesWith(collide: CompCollide) {
        println("CCollide.collidesWith $collide")
    }
}