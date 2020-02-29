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
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import kotlin.math.min

object SParticles : Setup() {

    // yes one for all
    val fireAnim = GAnim("fire", 0.3f)
    val blueAnim = GAnim("blue", 0.3f)
    val red = GGraphics.img("squares/square_red")
    val yellow = GGraphics.img("squares/square_yellow")

    init {
        fireAnim.addFrame(red)
        fireAnim.addFrame(GGraphics.img("squares/square_orange"))
        fireAnim.addFrame(yellow)

        blueAnim.addFrame(GGraphics.img("squares/square_purple"))
        blueAnim.addFrame(GGraphics.img("squares/square_blue"))
        blueAnim.addFrame(GGraphics.img("squares/square_cyan"))
    }

    fun cpuHearthParticle(world: World, x: Float, y: Float, w: Float, h: Float, angle: Float) {
        val p = world.create(Builder.bloodParticle)
        val space = p.space()
        space.setDim(w, h)
        space.setPos(x, y)
        p.layer().setLayer(Phases.Other)
        val ttl = p.ttl()
        ttl.remaining = 0.1f
        val bloomer = p.bloomer()
        bloomer.draw = GGraphics::drawFrontStreched
        bloomer.tr = red.front
        bloomer.angle = angle
    }

    fun cpuFootstep(world: World, x: Float, y: Float) {
        val p = world.create(Builder.particle)
        val space = p.space()
        space.setDim(1f, 1f)
        space.setPos(x, y)
        p.layer().setLayer(Phases.Other)
        val ttl = p.ttl()
        ttl.remaining = GRand.absGauss(0.5f)
        val draw = p.draw()
        draw.currentImg = yellow
    }

    fun cpuAttackParticle(world: World, x: Float, y: Float) {
        val p = world.create(Builder.bloodParticle)
        val space = p.space()
        space.setDim(1f, 1f)
        space.setPos(x, y)
        p.layer().setLayer(Phases.Other)
        val ttl = p.ttl()
        ttl.remaining = GRand.absGauss(0.5f)
        val bloomer = p.bloomer()
        bloomer.draw = GGraphics::drawFrontStreched
        bloomer.preDraw = {
            bloomer.tr = fireAnim.getKeyFrame(ttl.remaining * 8f).front
        }
    }

    val angleV = Vector2()
    fun chargingParticles(world: World, targetX: Float, targetY: Float, str: Float, anim: GAnim = fireAnim) {
        val p = world.create(Builder.bloodParticle)
        val bloomer = p.bloomer()
        val space = p.space()
        val dir = p.dir()
        val ttl = p.ttl()
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
            bloomer.tr = anim.getKeyFrame(ttl.remaining * 8f).front
        }

        p.particle().update = {
            dir.mul(0.97f)
            if (GKeyGlobalState.touched)
                space.move(targetX - space.x, targetY - space.y, GTime.playerDelta * 10f * normalizedStr * normalizedStr)
            angleV.set(space.x - space.oldX, space.y - space.oldY)
            bloomer.angle = (angleV.angle() + dir.angle) / 2f
        }
    }

    fun muzzle(world: World, centerX: Float, centerY: Float, dir: Vector2, str: Float = 1f) {
        val p = world.create(Builder.bloodParticle)
        val ttl = p.ttl()
        val bloomer = p.bloomer()
        val space = p.space()
        p.layer().setLayer(Phases.Other)
        p.dir().set(dir)
        p.dir().v.nor()
        p.dir().rotate(GRand.gauss(35f))
        p.dir().v.scl(GRand.absGauss(240f))
        space.setDim(1f + GRand.absGauss(3f), 1f)
        space.setPos(centerX + GRand.gauss(.01f) * str, centerY + GRand.gauss(.01f) * str)
//        ttl.remaining = GRand.absGauss(.1f)
        ttl.remaining = .09f
        bloomer.draw = GGraphics::drawFrontStreched
        bloomer.angle = p.dir().angle
        bloomer.preDraw = {
            bloomer.tr = blueAnim.getKeyFrame(ttl.remaining * 16f).front
        }
    }

    fun lollipopShoot(world: World, centerX: Float, centerY: Float, str: Float = 1f) {
        val p = world.create(Builder.bloodParticle)
        val ttl = p.ttl()
        val bloomer = p.bloomer()        
        val space = p.space()
        p.layer().setLayer(Phases.Other)
        space.setPos(centerX + GRand.gauss(1f) * str, centerY + GRand.gauss(1f) * str)
        ttl.remaining = MathUtils.clamp(1.2f - Vector2.dst2(space.centerX, space.centerY, centerX, centerY), 0.01f, 0.15f) / 2f
        bloomer.draw = GGraphics::drawFrontStreched
        bloomer.preDraw = {
            bloomer.tr = blueAnim.getKeyFrame(ttl.remaining * 16f).front
        }
    }

    val angleVec = Vector2(0f, 4f)
    fun lollipopTrail(world: World, centerX: Float, centerY: Float, angle: Float, str: Float = 1f) {
        val p = world.create(Builder.bloodParticle)
        val ttl = p.ttl()
        val bloomer = p.bloomer()
        val space = p.space()
        p.layer().setLayer(Phases.Other)
        angleVec.setAngleRad(angle)
        space.setPos(centerX + angleVec.x, centerY + angleVec.y)
        ttl.remaining = .01f + GRand.absGauss(0.15f)
        bloomer.draw = GGraphics::drawFrontStreched
        bloomer.preDraw = {
            bloomer.tr = blueAnim.getKeyFrame(ttl.remaining * 8f + GRand.gauss(0.1f)).front
        }
    }

    fun fireParticle(world: World, centerX: Float, centerY: Float, str: Float) {
        val p = world.create(Builder.bloodParticle)
        val bloomer = p.bloomer()
        val space = p.space()
        val dir = p.dir()
        val ttl = p.ttl()

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

        bloomer.tr = GGraphics.img("squares/square_red").front
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