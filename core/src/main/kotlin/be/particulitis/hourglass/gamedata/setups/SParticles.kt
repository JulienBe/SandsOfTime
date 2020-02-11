package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.common.GKeyGlobalState
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GSounds
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GAnim
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Phases
import com.artemis.World
import com.badlogic.gdx.math.Vector2
import kotlin.math.min

object SParticles : Setup() {

    // yes one for all
    val fireAnim = GAnim("prout", 0.3f)

    init {
        fireAnim.addFrame(GGraphics.img("square_red"))
        fireAnim.addFrame(GGraphics.img("square_orange"))
        fireAnim.addFrame(GGraphics.img("square_yellow"))
    }

    val angleV = Vector2()
    fun chargingParticles(world: World, targetX: Float, targetY: Float, str: Float) {
        val p = world.create(Builder.bloodParticle)
        val bloomer = p.bloomer()
        val space = p.space()
        val dir = p.dir()
        val ttl = p.ttl()
        var time = 0f
        val normalizedStr = 1f + min(str, 0.5f)

        p.layer().setLayer(Phases.Player)
        ttl.remaining = normalizedStr

        space.setPos(targetX + GRand.gauss(3f) * normalizedStr * normalizedStr, targetY + GRand.gauss(3f) * normalizedStr * normalizedStr)

        val dim1 = GRand.absGauss(1.5f)
        val dim2 = GRand.absGauss(1.5f)
        space.setDim(if (dim1 > dim2) dim1 else dim2, if (dim1 < dim2) dim1 / 2f else dim2 / 2f)

        ttl.remaining = 0.025f + GRand.absGauss(.2f)
        dir.add((space.x - targetX) * -20f, (space.y - targetY) * -20f)
        dir.rotate(-90f)

        bloomer.draw = GGraphics::drawFrontStreched
        bloomer.preDraw = {
            bloomer.tr = fireAnim.getKeyFrame(ttl.remaining * 8f).front
        }

        p.particle().update = {
            time += GTime.playerDelta
            dir.mul(0.97f)
            if (GKeyGlobalState.touched)
                space.move(targetX - space.x, targetY - space.y, GTime.playerDelta * 10f * normalizedStr * normalizedStr)
            angleV.set(space.x - space.oldX, space.y - space.oldY)
            bloomer.angle = (angleV.angle() + dir.angle) / 2f
        }
    }

    fun fireParticle(world: World, centerX: Float, centerY: Float, str: Float) {
        val p = world.create(Builder.bloodParticle)
        val bloomer = p.bloomer()
        val space = p.space()
        val dir = p.dir()
        val ttl = p.ttl()
        var time = 0f

        p.layer().setLayer(Phases.Other)

        space.setPos(centerX + GRand.gauss(0.1f) * str, centerY + GRand.gauss(0.1f) * str)

        val dim1 = GRand.absGauss(2f)
        val dim2 = GRand.absGauss(2f)
        space.setDim(if (dim1 > dim2) dim1 else dim2, if (dim1 < dim2) dim1 / 2f else dim2 / 2f)

        ttl.remaining = 0.02f + GRand.absGauss(.1f)
        dir.add((space.x - centerX) * 60f * str, (space.y - centerY) * 60f * str)

        bloomer.draw = GGraphics::drawFrontStreched
        bloomer.preDraw = {
            bloomer.tr = fireAnim.getKeyFrame(ttl.remaining * 8f).front
        }

        p.particle().update = {
            time += GTime.delta
            dir.mul(0.97f)
            dir.rotate(GTime.delta * 1000f * str)
            bloomer.angle = dir.angle
            space.move(dir, GTime.delta)
        }
    }

    fun explosionParticle(world: World, explosionCenterX: Float, explosionCenterY: Float, str: Float) {
        val p = world.create(Builder.bloodParticle)
        val bloomer = p.bloomer()
        val space = p.space()
        val dir = p.dir()
        val ttl = p.ttl()

        p.layer().setLayer(Phases.Other)

        space.setPos(explosionCenterX + GRand.gauss(1f), explosionCenterY + GRand.gauss(1f))

        val dim1 = GRand.absGauss(2f)
        val dim2 = GRand.absGauss(2f)
        space.setDim(if (dim1 > dim2) dim1 else dim2, if (dim1 < dim2) dim1 else dim2)

        ttl.remaining = GRand.absGauss(.5f)


        dir.add(GRand.gauss(str * 2f), GRand.gauss(str * 2f))

        bloomer.tr = GGraphics.img("square").front
        bloomer.preDraw = {
            bloomer.angle = dir.angle
            bloomer.tr = fireAnim.getKeyFrame(ttl.remaining * 8f).front
        }
        GSounds.explosion1.play()


        p.particle().update = {
            dir.mul(.97f)
        }
    }
}