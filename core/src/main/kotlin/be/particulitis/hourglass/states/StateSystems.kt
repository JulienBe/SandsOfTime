package be.particulitis.hourglass.states

import be.particulitis.hourglass.system.*
import com.artemis.BaseSystem

enum class StateSystems(vararg val systems: Pair<Class<out BaseSystem>, Boolean>) {
    PLAYER_DEAD(
            Pair(SysTime::class.java,               false),
            Pair(SysControl::class.java,            false),
            Pair(SysCharMovement::class.java,       false),
            Pair(SysTargetAcquisition::class.java,  false),
            Pair(SysTargetSeek::class.java,         false),
            Pair(SysDirMovement::class.java,        false),
            Pair(SysShooter::class.java,            false),
            Pair(SysTtl::class.java,                false),
            Pair(SysCollider::class.java,           false),
            Pair(SysDamage::class.java,             false),
            Pair(SysClampPos::class.java,           false),
            Pair(SysSpawner::class.java,            false),
            Pair(SysDrawer::class.java,             true),
            Pair(SysClearActions::class.java,       true),
            Pair(SysDead::class.java,               false),
            Pair(SysStartGame::class.java,          true)
    ),
    RUNNING(
            Pair(SysTime::class.java,               true),
            Pair(SysControl::class.java,            true),
            Pair(SysCharMovement::class.java,       true),
            Pair(SysTargetAcquisition::class.java,  true),
            Pair(SysTargetSeek::class.java,         true),
            Pair(SysDirMovement::class.java,        true),
            Pair(SysShooter::class.java,            true),
            Pair(SysTtl::class.java,                true),
            Pair(SysCollider::class.java,           true),
            Pair(SysDamage::class.java,             true),
            Pair(SysClampPos::class.java,           true),
            Pair(SysDrawer::class.java,             true),
            Pair(SysClearActions::class.java,       true),
            Pair(SysDead::class.java,               true),
            Pair(SysSpawner::class.java,            true),
            Pair(SysStartGame::class.java,          false)
    ),
    PAUSED(
            Pair(SysTime::class.java,               false),
            Pair(SysControl::class.java,            false),
            Pair(SysCharMovement::class.java,       false),
            Pair(SysTargetAcquisition::class.java,  false),
            Pair(SysTargetSeek::class.java,         false),
            Pair(SysDirMovement::class.java,        false),
            Pair(SysShooter::class.java,            false),
            Pair(SysTtl::class.java,                false),
            Pair(SysCollider::class.java,           false),
            Pair(SysDamage::class.java,             false),
            Pair(SysClampPos::class.java,           false),
            Pair(SysSpawner::class.java,            false),
            Pair(SysDrawer::class.java,             true),
            Pair(SysClearActions::class.java,       true),
            Pair(SysDead::class.java,               true),
            Pair(SysStartGame::class.java,          false)
    )
}
