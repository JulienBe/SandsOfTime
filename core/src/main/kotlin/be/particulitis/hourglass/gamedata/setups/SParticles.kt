package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.common.GKeyGlobalState
import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GSounds
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GGraphics
import be.particulitis.hourglass.gamedata.graphics.Frames
import be.particulitis.hourglass.common.puppet.GAnim
import be.particulitis.hourglass.comp.CompHp
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.Builder
import be.particulitis.hourglass.gamedata.Phases
import com.artemis.World
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import kotlin.math.min

object SParticles : Setup() {

    // yes one for all
    val red = GGraphics.img("squares/square_red")
    val yellow = GGraphics.img("squares/square_yellow")
    val cpuSpawn = GAnim(Frames.CPU_SPAWN)
    val fireAnim = GAnim(Frames.FIRE, .1f, Animation.PlayMode.NORMAL)
    val blueAnim = GAnim(Frames.BLUE, .1f, Animation.PlayMode.NORMAL)
    val pinkAnim = GAnim(Frames.PINK, .1f, Animation.PlayMode.NORMAL)
    val normalUp = GGraphics.tr("normal_up")
    val normalRight = GGraphics.tr("normal_right")
    val normalDown = GGraphics.tr("normal_down")
    val normalLeft = GGraphics.tr("normal_left")
    val normals = arrayListOf(normalUp, normalRight, normalDown, normalLeft)

    fun trailTarget(world: World, x: Float, y: Float, anim: GAnim, targetX: Float, targetY: Float, ttlMul: Float = 1f) {
        val p = world.create(Builder.trailParticleBloomer)
        val space = p.space()
        space.setPos(x, y)
        val ttl = p.ttl()
        val originalTtl = (0.07f + GRand.absGauss(0.1f)) * ttlMul
        ttl.remaining = originalTtl
        val bloomer = p.bloomer()

        bloomer.preDraw = {
            bloomer.tr = anim.getKeyFrame(ttl.remaining * 16f).front
            space.move((targetX - space.x), (targetY - space.y), GTime.delta * 2f)
            trailCrowler(world, space.x, space.y, anim, originalTtl / 4f)
        }
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.draw(bloomer.tr, space)
        }
        p.layer().setLayer(Phases.Other)
    }

    fun trailCrowler(world: World, x: Float, y: Float, anim: GAnim, ttlMul: Float = 1f) {
        val p = world.create(Builder.trailParticleBloomer)
        val space = p.space()
        space.setPos(x, y)
        val ttl = p.ttl()
        ttl.remaining = (0.07f + GRand.absGauss(0.1f)) * ttlMul
        val bloomer = p.bloomer()

        val growH = GRand.int(-1, 1).toFloat() * 5f
        val growW = if (growH == 0f)
                        if (GRand.nextBoolean())
                            5f else -5f
                    else
                        0f

        bloomer.preDraw = {
            bloomer.tr = anim.getKeyFrame(ttl.remaining * 8f).front
            space.setDim(space.w + GTime.delta * growW, space.h + GTime.delta * growH)
        }
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.draw(bloomer.tr, space)
        }
        p.layer().setLayer(Phases.Other)
    }

    fun trailFixed(world: World, x: Float, y: Float, anim: GAnim, ttlToSet: Float = .5f) {
        val p = world.create(Builder.trailParticleBloomer)
        p.space().setPos(x, y)
        val ttl = p.ttl()
        ttl.remaining = ttlToSet
        val bloomer = p.bloomer()
        bloomer.preDraw = {
            bloomer.tr = anim.getKeyFrame(ttl.remaining * 8f).front
        }
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.draw(bloomer.tr, space.x, space.y, 1f, 1f)
        }
        p.layer().setLayer(Phases.Other)
    }

    fun trail(world: World, x: Float, y: Float, anim: GAnim, ttlMul: Float = 1f) {
        val p = world.create(Builder.trailParticleBloomer)
        p.space().setPos(x, y)
        val ttl = p.ttl()
        ttl.remaining = (0.07f + GRand.absGauss(0.1f)) * ttlMul
        val bloomer = p.bloomer()
        bloomer.preDraw = {
            bloomer.tr = anim.getKeyFrame(ttl.remaining * 8f).front
        }
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.draw(bloomer.tr, space.x, space.y, 1f, 1f)
        }
        p.layer().setLayer(Phases.Other)
    }

    fun spawnTransition(world: World, x: Float, y: Float, hp: CompHp) {
        val p = world.create(Builder.trailParticleBloomer)
        val space = p.space()
        space.setPos(x, y)
        val ttl = p.ttl()
        ttl.remaining = 8f
        val bloomer = p.bloomer()
        var time = 0f
        bloomer.preDraw = {
            time += GTime.delta * 1.5f
            bloomer.angle = 90f
            bloomer.tr = cpuSpawn.getKeyFrame(time).front
            if (cpuSpawn.isFinished(time) || hp.hp <= 0)
                ttl.remaining = -1f
        }
        p.layer().setLayer(Phases.Other)
    }

    fun randomNormal(world: World, x: Float, y: Float) {
        val p = world.create(Builder.trailParticle)
        p.space().setDim(1f, 1f)
        p.space().setPos(x, y)
        p.ttl().remaining = 50f + GRand.absGauss(10f)
        p.draw().drawFront = { batch, space -> }
        p.draw().drawOcc = { batch, space -> }
        val normal = normals.random()
        p.draw().drawNormal = { batch, space ->
            batch.draw(normal, space)
        }
    }

    private val moveVector = Vector2()
    fun spawnAnim(world: World, x: Float, y: Float) {
        val p = world.create(Builder.trailParticleBloomer)
        val space = p.space()
        space.setPos(x + GRand.gauss(20f), y + GRand.gauss(20f))
        val ttl = p.ttl()
        ttl.remaining = 8f
        val bloomer = p.bloomer()
        val speed = Vector2.dst(space.x, space.y, x, y) / 0.8f
        bloomer.tr = yellow.front
        bloomer.preDraw = {
            moveVector.set(0f, 0f)
            moveVector.x = getXMove(space, x)
            moveVector.y = getYMove(space, y)
            moveVector.nor()
            if (Vector2.dst2(space.x, space.y, x, y) < 1.2f)
                ttl.remaining = 0f
            space.move(moveVector.x, moveVector.y, GTime.delta * speed)
            trail(world, space.x, space.y, fireAnim)
        }
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.draw(bloomer.tr, space.x, space.y, 1f, 1f)
        }
        p.layer().setLayer(Phases.Other)
    }

    private fun getYMove(space: CompSpace, y: Float): Float {
        return when {
            space.y < y -> 1f
            space.y > y -> -1f
            else -> 0f
        }
    }

    private fun getXMove(space: CompSpace, x: Float): Float {
        return when {
            space.x < x -> 1f
            space.x > x -> -1f
            else -> 0f
        }
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
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontStreched(bloomer, space)
        }
        bloomer.tr = red.front
        bloomer.angle = angle
    }

    fun cpuFootstep(world: World, x: Float, y: Float) {
        val p = world.create(Builder.bloodParticle)
        val space = p.space()
        space.setDim(1f, 1f)
        space.setPos(x, y)
        p.layer().setLayer(Phases.Other)
        val ttl = p.ttl()
        ttl.remaining = GRand.absGauss(0.25f)
//        val draw = p.draw()
//        draw.currentImg = yellow
        val bloom = p.bloomer()
        bloom.tr = yellow.front
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
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontStreched(bloomer, space)
        }
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

        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontStreched(bloomer, space)
        }
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
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontStreched(bloomer, space)
        }
        bloomer.angle = p.dir().angle
        bloomer.preDraw = {
            bloomer.tr = blueAnim.getKeyFrame(ttl.remaining * 16f).front
        }
    }

    fun lollipopShoot(world: World, centerX: Float, centerY: Float, pink: Boolean, str: Float = 1f) {
        val p = world.create(Builder.bloodParticle)
        val ttl = p.ttl()
        val bloomer = p.bloomer()
        val space = p.space()
        p.layer().setLayer(Phases.Other)
        space.setPos(centerX + GRand.gauss(1f) * str, centerY + GRand.gauss(1f) * str)
        ttl.remaining = MathUtils.clamp(1.2f - Vector2.dst2(space.centerX, space.centerY, centerX, centerY), 0.01f, 0.15f) / 2f
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontStreched(bloomer, space)
        }
        val anim = if (pink) pinkAnim else blueAnim
        bloomer.preDraw = {
            bloomer.tr = anim.getKeyFrame(ttl.remaining * 16f).front
        }
    }

    val angleVec = Vector2(0f, 4f)
    fun lollipopTrail(world: World, centerX: Float, centerY: Float, angle: Float, pink: Boolean, str: Float = 1f) {
        val p = world.create(Builder.bloodParticle)
        val ttl = p.ttl()
        val bloomer = p.bloomer()
        val space = p.space()
        p.layer().setLayer(Phases.Other)
        angleVec.setAngleRad(angle)
        space.setPos(centerX + angleVec.x, centerY + angleVec.y)
        ttl.remaining = .01f + GRand.absGauss(0.15f)
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontStreched(bloomer, space)
        }
        val anim = if (pink) pinkAnim else blueAnim
        bloomer.preDraw = {
            bloomer.tr = anim.getKeyFrame(ttl.remaining * 8f + GRand.gauss(0.1f)).front
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

        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontStreched(bloomer, space)
        }
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
        space.setDim(if (dim1 > dim2) dim1 else dim2, if (dim1 < dim2) dim1 / 2f else dim2 / 2f)

        ttl.remaining = GRand.absGauss(.5f)


        dir.add(GRand.gauss(str * 2f), GRand.gauss(str * 2f))

        bloomer.tr = GGraphics.img("squares/square_red").front
        bloomer.preDraw = {
            bloomer.angle = dir.angle
            bloomer.tr = fireAnim.getKeyFrame(ttl.remaining * 8f).front
            trail(world, space.centerX, space.centerY, fireAnim)
        }
        bloomer.draw = { batch: GGraphics, space: CompSpace ->
            batch.drawFrontStreched(bloomer, space)
        }
        GSounds.explosion1.play()


        p.particle().update = {
            dir.mul(.97f)
        }
    }
}