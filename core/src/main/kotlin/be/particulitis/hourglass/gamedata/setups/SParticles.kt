package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.gamedata.Layers
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.World

object SParticles {

    fun explosionParticle(id: Int, world: World, explosionCenterX: Float, explosionCenterY: Float, str: Float) {
        val p = world.getEntity(id)
        val draw = p.getComponent(CompDraw::class.java)
        val space = p.getComponent(CompSpace::class.java)
        val particle = p.getComponent(CompParticle::class.java)
        val ttl = p.getComponent(CompTtl::class.java)
        val dir = p.getComponent(CompDir::class.java)
        val light = p.getComponent(CompLight::class.java)
        val layer = p.getComponent(CompLayer::class.java)

        layer.setLayer(Layers.Other)

        space.setPos(explosionCenterX + GRand.gauss(1f), explosionCenterY + GRand.gauss(1f))

        val w = GRand.absGauss(1f)
        space.setDim(w, w)

        ttl.remaining = GRand.absGauss(.5f)

        dir.add(GRand.gauss(str), GRand.gauss(str))

        particle.update = {
            dir.mul(.97f)
        }

        draw.drawingStyle = { batch ->
            DrawMethods.basic(space, draw, batch)
        }
    }

}