package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.common.GHelper
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GSounds
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Layers
import com.artemis.World
import com.badlogic.gdx.math.MathUtils
import kotlin.math.roundToInt

object SParticles : Setup() {

    fun fireParticle(world: World, centerX: Float, centerY: Float, str: Float) {
        val textures = arrayListOf(GGraphics.tr("square_yellow"), GGraphics.tr("square_orange"), GGraphics.tr("square_red"))
        val p = world.create(Builder.bloodParticle)
        val draw = p.draw()
        val space = p.space()
        val dir = p.dir()
        val ttl = p.ttl()
        var time = 0f

        p.layer().setLayer(Layers.Other)

        space.setPos(centerX + GRand.gauss(2f), centerY + GRand.gauss(2f))

        val dim1 = GRand.absGauss(2f)
        val dim2 = GRand.absGauss(2f)
        space.setDim(if (dim1 > dim2) dim1 else dim2, if (dim1 < dim2) dim1 / 2f else dim2 / 2f)

        ttl.remaining = GRand.absGauss(.5f)
        dir.add((space.x - centerX) * 10f, (space.y - centerY) * 10f)

        draw.normal = GGraphics.nor("square")
        draw.drawFront = {
            it.draw(textures[MathUtils.clamp((time * 3f).roundToInt(), 0, textures.size - 1)], space, draw.angle)
        }
        draw.drawNormal = {
            it.draw(draw.normal, space, draw.angle)
        }

        p.particle().update = {
            dir.mul(.97f)
            time += GTime.delta
            dir.rotate(GTime.delta * 1000f)
            draw.angle = dir.angle
        }
    }

    fun explosionParticle(world: World, explosionCenterX: Float, explosionCenterY: Float, str: Float) {
        val p = world.create(Builder.bloodParticle)
        val draw = p.draw()
        val space = p.space()
        val dir = p.dir()

        p.layer().setLayer(Layers.Other)

        space.setPos(explosionCenterX + GRand.gauss(1f), explosionCenterY + GRand.gauss(1f))

        val dim1 = GRand.absGauss(2f)
        val dim2 = GRand.absGauss(2f)
        space.setDim(if (dim1 > dim2) dim1 else dim2, if (dim1 < dim2) dim1 else dim2)

        p.ttl().remaining = GRand.absGauss(.5f)


        dir.add(GRand.gauss(str * 2f), GRand.gauss(str * 2f))

        GGraphics.setupTextures(draw, "square")
        draw.drawFront = {
            draw.angle = dir.angle
            it.draw(draw.texture, space, draw.angle)
        }
        draw.drawNormal = {
            it.draw(draw.normal, space, draw.angle)
        }
        GSounds.explosion1.play()


        p.particle().update = {
            dir.mul(.97f)
        }
    }

}