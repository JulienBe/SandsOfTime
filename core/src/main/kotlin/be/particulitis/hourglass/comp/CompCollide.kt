package be.particulitis.hourglass.comp

import be.particulitis.hourglass.Ids
import be.particulitis.hourglass.common.GSide
import ktx.collections.gdxMapOf

class CompCollide : Comp() {

    var dmgToInflict = 0
        private set
    var dmgToTake = 0
        private set
    var dmgTakenTime = 0f
        private set
    var id = 0
        private set
    var collidesWith = 0
        private set
    var collision = { otherCollide: CompCollide, otherSpace: CompSpace, side: GSide -> }
    var fromOtherCollider = { otherCollide: CompCollide, otherSpace: CompSpace, side: GSide -> }
    var collidingMap = gdxMapOf(Ids.player.id to { entity: Int, otherEntity: Int, meCollide: CompCollide, meSpace: CompSpace, otherCollide: CompCollide, otherSpace: CompSpace, side: GSide -> })

    init {
        Ids.values().forEach {
            collidingMap.put(it.id) { entity: Int, otherEntity: Int, meCollide: CompCollide, meSpace: CompSpace, otherCollide: CompCollide, otherSpace: CompSpace, side: GSide -> }
        }
    }

    fun setDmgToInflict(dmg: Int) {
        this.dmgToInflict = dmg
    }

    fun setDmgToTake(dmg: Int) {
        this.dmgToTake = dmg
    }

    fun setDmgTakenTime(time: Float) {
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

    override fun reset() {
        super.reset()
        id = 0
        collidesWith = 0
        dmgTakenTime = 0f
        dmgToInflict = 1
    }
}