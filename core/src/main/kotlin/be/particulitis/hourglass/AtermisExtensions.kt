package be.particulitis.hourglass

import be.particulitis.hourglass.comp.*
import be.particulitis.hourglass.comp.draw.CompBloomer
import be.particulitis.hourglass.comp.draw.CompDraw
import be.particulitis.hourglass.comp.draw.CompUndertrail
import be.particulitis.hourglass.comp.ui.CompButton
import be.particulitis.hourglass.comp.ui.CompPrettyUi
import be.particulitis.hourglass.comp.ui.CompTxt
import com.artemis.*
import com.artemis.managers.TagManager
import com.artemis.utils.Bag
import com.artemis.utils.IntBag
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty

/**
 * indicates an invalid/unset entity id
 */
const val INVALID_ENTITY_ID = -1

inline fun isValidEntity(entityId: Int) = entityId != INVALID_ENTITY_ID
inline fun isInvalidEntity(entityId: Int) = entityId == INVALID_ENTITY_ID

inline fun <reified T : BaseSystem> World.system() =
        getSystem(T::class.java)!!


/**
 * Denotes that a component property should not be copied
 */
@Retention
@Target(AnnotationTarget.PROPERTY)
annotation class DoNotCopy

interface ExtendedComponent<T : ExtendedComponent<T>> {

    /**
     * copy a component (similar to copy constructor)
     *
     * @param other
     *         component to copy from, into this instance
     */
    fun copyFrom(other: T): Unit

    // todo: we may actually want to instead have a combine function,
    //whose result is the combination of both
    // (otherwise how do we know how to combine them properly?)
    fun canCombineWith(other: T): Boolean

    fun printString(): String {
        return getCache(javaClass).printProperties.map {
            "${javaClass.simpleName}.${it.name} = ${it.getter.call(this)}"
        }
                .joinToString(separator = "\n", postfix = "\n")
    }

    fun defaultPrintString(): String {
        return getCache(javaClass).printProperties.map {
            "${javaClass.simpleName}.${it.name} = ${it.getter.call(this)}"
        }
                .joinToString(separator = "\n", postfix = "\n")
    }
}

/**
 * Denotes that a component property should not be printed on-screen in debug mode
 */
@Retention
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER)
annotation class DoNotPrint

fun World.getComponentsForEntity(entity: Int): Bag<Component> {
    val bag = Bag<Component>()
    componentManager.getComponentsFor(entity, bag)
    return bag
}

fun World.entities(aspect: Aspect.Builder): IntBag =
        this.aspectSubscriptionManager.get(aspect).entities

//object ArtemisExtensions {
fun allOf(vararg types: KClass<out Component>): Aspect.Builder =
        Aspect.all(types.map { it.java })

fun anyOf(vararg types: KClass<out Component>): Aspect.Builder =
        Aspect.one(types.map { it.java })

fun noneOf(vararg types: KClass<out Component>): Aspect.Builder =
        Aspect.exclude(types.map { it.java })

fun <T : Component> ComponentMapper<T>.opt(entityId: Int): T? = if (has(entityId)) get(entityId) else null

inline fun <T : Component> ComponentMapper<T>.ifPresent(entityId: Int, function: (T) -> Unit): Unit {
    //fixme change has to just 1 get, since has calls get anyway
    if (has(entityId))
        function(get(entityId))
}

fun TagManager.opt(entityId: Int): String? {
    return this.getTag(entityId) ?: null
}

/*
inline fun <T> IntBag.forEachIndexed(action: (Int, T) -> Unit): Unit {
    var index = 0
    for (item in this) action(index++, item)
}
*/

inline fun IntBag.forEach(action: (Int) -> Unit): Unit {
    for (i in indices) action(this.get(i))
}


//public inline fun <T> Array<out T>.forEach(action: (T) -> Unit): Unit {
//    for (element in this) action(element)
//}

fun IntBag.toMutableList(): MutableList<Int> {
    val list = mutableListOf<Int>()
    this.forEach { list.add(it) }
    return list
}

val IntBag.indices: IntRange get() = 0..size() - 1
val <T : Any> Bag<T>.indices: IntRange get() = 0..size() - 1

/*
inline fun <T> Array<out T>.forEachIndexed(action: (Int, T) -> Unit): Unit {
    var index = 0
    for (item in this) action(index++, item)
}
*/

private class MapperProperty<T : Component>(val cType: Class<T>) : ReadOnlyProperty<BaseSystem, ComponentMapper<T>> {
    private var cachedMapper: ComponentMapper<T>? = null

    override fun getValue(thisRef: BaseSystem, property: KProperty<*>): ComponentMapper<T> {
        if (cachedMapper == null) {
            val worldField = BaseSystem::class.java.getDeclaredField("world")
            worldField.isAccessible = true

            val world = worldField.get(thisRef) as World?
            world ?: throw IllegalStateException("world is not initialized yet")
            cachedMapper = world.getMapper(cType)
        }
        return cachedMapper!!
    }
}

private class SystemProperty<T : BaseSystem>(val cType: Class<T>) : ReadOnlyProperty<BaseSystem, T> {
    private var cachedSystem: T? = null

    override fun getValue(thisRef: BaseSystem, property: KProperty<*>): T {
        if (cachedSystem == null) {
            val worldField = BaseSystem::class.java.getDeclaredField("world")
            worldField.isAccessible = true

            val world = worldField.get(thisRef) as World?
            world ?: throw IllegalStateException("world is not initialized yet")

            cachedSystem = world.getSystem(cType)
        }
        return cachedSystem!!
    }
}

/**
 * Gets a delegate that returns the `ComponentMapper` for the given component cType.
 *
 * @param T the component cType.
 */
inline fun <reified T : Component> BaseSystem.mapper(): ReadOnlyProperty<BaseSystem, ComponentMapper<T>> = mapper(
        T::class)

/**
 * Gets a delegate that returns the `ComponentMapper` for the given component cType.
 *
 * @param T the component cType.
 * @param cType the component class.
 */
fun <T : Component> BaseSystem.mapper(cType: KClass<T>): ReadOnlyProperty<BaseSystem, ComponentMapper<T>> = MapperProperty<T>(
        cType.java)

/**
 * Gets a delegate that returns the `ComponentMapper` for the given component cType, and adds the component cType
 * to the system's aspect configuration. Note that this must be called from constructor code, or it won't be effective!
 *
 * @param T the component cType.
 */
inline fun <reified T : Component> BaseEntitySystem.require(): ReadOnlyProperty<BaseSystem, ComponentMapper<T>> = require(
        T::class)

/**
 * Gets a delegate that returns the `ComponentMapper` for the given component cType, and adds the component cType
 * to the system's aspect configuration. Note that this must be called from constructor code, or it won't be effective!
 *
 * @param T the component cType.
 * @param cType the component class.
 */
fun <T : Component> BaseEntitySystem.require(cType: KClass<T>): ReadOnlyProperty<BaseSystem, ComponentMapper<T>> {
    val aspectConfigurationField = BaseEntitySystem::class.java.getDeclaredField("aspectConfiguration")
    aspectConfigurationField.isAccessible = true

    val aspectConfiguration = aspectConfigurationField.get(this) as Aspect.Builder
    aspectConfiguration.all(cType.java)

    return MapperProperty<T>(cType.java)
}

/**
 * Gets a delegate that returns the `EntitySystem` of the given cType.
 *
 * @param T the system cType.
 */
inline fun <reified T : BaseSystem> BaseSystem.system(): ReadOnlyProperty<BaseSystem, T> = system(T::class)

/**
 * Gets a delegate that returns the `EntitySystem` of the given cType.
 *
 * @param T the system cType.
 * @param cType the system class.
 */
fun <T : BaseSystem> BaseSystem.system(cType: KClass<T>): ReadOnlyProperty<BaseSystem, T> = SystemProperty<T>(cType.java)

private val cacheByType = hashMapOf<Class<*>, PropertyCache>()

private fun getCache(clazz: Class<*>): PropertyCache =
        cacheByType.getOrPut(clazz, { PropertyCache(clazz.kotlin) })

/**
 * Cache that stores properties of component implementations.
 */
private class PropertyCache(clazz: KClass<*>) {
    val copyProperties = clazz.members.mapNotNull { it as? KMutableProperty }
            .filter { !it.annotations.any { it.annotationClass == DoNotCopy::class } }.toTypedArray()

    val printProperties = clazz.members.mapNotNull { it as? KProperty }
            .filter {
                !it.annotations.any { it.annotationClass == DoNotPrint::class } &&
                        !it.getter.annotations.any { it.annotationClass == DoNotPrint::class }
            }.toTypedArray()
}

/**
 * copy a component (similar to copy constructor)
 *
 * @param other
 *         component to copy from, into this instance
 */
fun <T : Component> T.copyFrom(other: T) {
    if (this is ExtendedComponent<*>) {
        this.internalCopyFrom<InternalExtendedComponent>(other)
    } else {
        this.defaultCopyFrom(other)
    }
}

fun <T : Component> T.canCombineWith(other: T): Boolean {
    if (this is ExtendedComponent<*>) {
        return this.internalCanCombineWith<InternalExtendedComponent>(other)
    }
    return false
}

fun <T : Component> T.printString(): String {
    if (this is ExtendedComponent<*>) {
        return this.internalPrintString<InternalExtendedComponent>()
    }
    return "ERROR! can't print string of this component. It is not an ExtendedComponent. " +
            "Please derive this component (${this.javaClass}) from ExtendedComponent\n"
}

/**
 * copy a component (similar to copy constructor)
 *
 * @param other
 *         component to copy from, into this instance
 */
fun <T : Component> T.defaultCopyFrom(other: T): Unit {
    getCache(javaClass).copyProperties.forEach { it.setter.call(this, it.getter.call(other)) }
}

// Just hacking around Kotlin generics...
@Suppress("UNCHECKED_CAST")
private fun <T : ExtendedComponent<T>> Any.internalCopyFrom(other: Any) {
    (this as T).copyFrom(other as T)
}

@Suppress("UNCHECKED_CAST")
private fun <T : ExtendedComponent<T>> Any.internalCanCombineWith(other: Any) =
        (this as T).canCombineWith(other as T)

@Suppress("UNCHECKED_CAST")
private fun <T : ExtendedComponent<T>> Any.internalPrintString() =
        (this as T).printString()

private class InternalExtendedComponent : ExtendedComponent<InternalExtendedComponent> {
    override fun canCombineWith(other: InternalExtendedComponent): Boolean =
            throw TODO("function not yet implemented")

    override fun copyFrom(other: InternalExtendedComponent) =
            throw TODO("function not yet implemented")

    override fun printString() =
            throw TODO("function not yet implemented")
}


fun World.create(arch: ArchetypeBuilder): Entity {
    return getEntity(create(arch.build(this)))
}
fun Entity.act(): CompAct {
    return getComponent(CompAct::class.java)
}
fun Entity.space(): CompSpace {
    return getComponent(CompSpace::class.java)
}
fun Entity.bloomer(): CompBloomer {
    return getComponent(CompBloomer::class.java)
}
fun Entity.draw(): CompDraw {
    return getComponent(CompDraw::class.java)
}
fun Entity.collide(): CompCollide {
    return getComponent(CompCollide::class.java)
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
fun Entity.layer(): CompTimePhase {
    return getComponent(CompTimePhase::class.java)
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
fun Entity. txt(): CompTxt {
    return getComponent(CompTxt::class.java)
}
fun Entity. control(): CompControl {
    return getComponent(CompControl::class.java)
}
fun Entity. charMvt(): CompCharMovement {
    return getComponent(CompCharMovement::class.java)
}
fun Entity. side(): CompSide {
    return getComponent(CompSide::class.java)
}
fun Entity. undertrail(): CompUndertrail {
    return getComponent(CompUndertrail::class.java)
}
// TODO: Might want to introduce PrintableComponent interface
//fun <T : Component> T.printString(): String {
//    this
//    return this.defaultPrintString()
//}

//fun <T : ExtendedComponent> T.defaultPrintString(): String =
//        getCache(javaClass).printProperties.map { "${javaClass.simpleName}.${it.t} = ${it.getter.call(this)}" }
//                .joinToString(separator = "\n", postfix = "\n")
