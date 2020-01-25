package be.particulitis.hourglass.gamedata.setups

import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.comp.ui.CompButton
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import com.artemis.ArchetypeBuilder
import com.artemis.Entity
import com.artemis.World

open class Setup {
    fun World.create(arch: ArchetypeBuilder): Entity {
        return getEntity(create(arch.build(this)))
    }
    fun Entity.space(): CompSpace {
        return getComponent(CompSpace::class.java)
    }
    fun Entity.occluder(): CompOccluder {
        return getComponent(CompOccluder::class.java)
    }
    fun Entity.draw(): CompDraw {
        return getComponent(CompDraw::class.java)
    }
    fun Entity.collide(): CompCollide {
        return getComponent(CompCollide::class.java)
    }
    fun Entity.light(): CompLight {
        return getComponent(CompLight::class.java)
    }
    fun Entity.emitter(): CompParticleEmitter {
        return getComponent(CompParticleEmitter::class.java)
    }
    fun Entity.targetSeek(): CompTargetSeek {
        return getComponent(CompTargetSeek::class.java)
    }
    fun Entity.targetFollow(): CompTargetFollow {
        return getComponent(CompTargetFollow::class.java)
    }
    fun Entity.dir(): CompDir {
        return getComponent(CompDir::class.java)
    }
    fun Entity.layer(): CompLayer {
        return getComponent(CompLayer::class.java)
    }
    fun Entity.shooter(): CompShooter {
        return getComponent(CompShooter::class.java)
    }
    fun Entity.hp(): CompHp {
        return getComponent(CompHp::class.java)
    }
    fun Entity.ttl(): CompTtl {
        return getComponent(CompTtl::class.java)
    }
    fun Entity.particle(): CompParticle {
        return getComponent(CompParticle::class.java)
    }
    fun Entity.prettyUi(): CompPrettyUi {
        return getComponent(CompPrettyUi::class.java)
    }
    fun Entity. button(): CompButton {
        return getComponent(CompButton::class.java)
    }
    fun Entity. control(): CompControl {
        return getComponent(CompControl::class.java)
    }
    fun Entity. charMvt(): CompCharMovement {
        return getComponent(CompCharMovement::class.java)
    }
}
