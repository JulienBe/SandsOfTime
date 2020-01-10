package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Layers
import be.particulitis.hourglass.gamedata.graphics.DrawMethods
import com.artemis.World

object SParticles : Setup() {

    fun explosionParticle(world: World, explosionCenterX: Float, explosionCenterY: Float, str: Float) {
        val p = world.create(Builder.explosionParticle)
        val draw = p.draw()
        val space = p.space()
        val dir = p.dir()

        p.layer().setLayer(Layers.Other)

        space.setPos(explosionCenterX + GRand.gauss(1f), explosionCenterY + GRand.gauss(1f))

        val w = GRand.absGauss(1f)
        space.setDim(w, w)

        p.ttl().remaining = GRand.absGauss(.5f)

        dir.add(GRand.gauss(str), GRand.gauss(str))

        p.particle().update = {
            dir.mul(.97f)
        }

        draw.drawingStyle = { batch, tr ->
            DrawMethods.basic(space, draw, batch)
        }
    }

}