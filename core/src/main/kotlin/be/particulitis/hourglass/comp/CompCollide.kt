package be.particulitis.hourglass.comp

import com.artemis.PooledComponent

class CompCollide : PooledComponent() {
    override fun reset() {
    }

    fun collidesWith(collide: CompCollide) {
        println("CCollide.collidesWith $collide")
    }
}