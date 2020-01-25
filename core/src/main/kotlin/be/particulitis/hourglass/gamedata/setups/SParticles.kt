package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GSounds
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Layers
import com.artemis.World

object SParticles : Setup() {

    fun explosionParticle(world: World, explosionCenterX: Float, explosionCenterY: Float, str: Float) {
        val p = world.create(Builder.explosionParticle)
        val draw = p.draw()
        val space = p.space()
        val dir = p.dir()
        var time = 0f

        p.layer().setLayer(Layers.Other)

        space.setPos(explosionCenterX + GRand.gauss(1f), explosionCenterY + GRand.gauss(1f))

        val dim1 = GRand.absGauss(2f)
        val dim2 = GRand.absGauss(2f)
        space.setDim(if (dim1 > dim2) dim1 else dim2, if (dim1 < dim2) dim1 else dim2)

        p.ttl().remaining = GRand.absGauss(.5f)


        dir.add(GRand.gauss(str * 2f), GRand.gauss(str * 2f))


        GGraphics.setupTextures(draw, "square")
        draw.drawFront = {
            draw.normalAngle = dir.angle
            it.draw(draw.texture, space, draw.normalAngle)
        }
        draw.drawNormal = {
            it.draw(draw.normal, space, draw.normalAngle)
        }
        GSounds.explosion1.play()


        p.particle().update = {
            dir.mul(.97f)
            time += GTime.delta
        }
    }

}