package be.particulitis.hourglass.system

import be.particulitis.hourglass.common.GRand
import be.particulitis.hourglass.common.GTime
import be.particulitis.hourglass.common.drawing.GResolution
import be.particulitis.hourglass.comp.CompSpace
import be.particulitis.hourglass.gamedata.setups.SSpawns
import com.artemis.BaseSystem
import com.artemis.ComponentMapper

class SysSpawner : BaseSystem() {

    private lateinit var mSpace: ComponentMapper<CompSpace>
    private var nextSpawn = 0f

    override fun processSystem() {
        if (nextSpawn < GTime.time) {
            SSpawns.cpuSpawn(world, GRand.float(30f, GResolution.areaW - 30f), GRand.float(30f, GResolution.areaH - 30f))
            nextSpawn = GTime.time + spawnRate
        }
    }

    companion object {
        const val spawnRate = 4f
    }
}