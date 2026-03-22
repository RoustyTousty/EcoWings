package me.roustytousty.elytrapvp.services.region

import me.roustytousty.elytrapvp.data.configs.RegionConfig
import org.bukkit.Location

class RegionService {

    private val regions = mutableMapOf<String, Pair<Location, Location>>()

    init {
        load()
    }

    fun load() {
        val config = RegionConfig.getConfig()

        val section = config.getConfigurationSection("regions") ?: return

        for (key in section.getKeys(false)) {
            val region = RegionConfig.getRegionPositions(key) ?: continue
            regions[key] = region
        }
    }

    fun get(region: String): Pair<Location, Location>? {
        return regions[region]
    }

    fun isInRegion(loc: Location, region: String): Boolean {
        val (pos1, pos2) = regions[region] ?: return false

        if (loc.world != pos1.world) return false

        val (minX, maxX) = listOf(pos1.blockX, pos2.blockX).sorted()
        val (minY, maxY) = listOf(pos1.blockY, pos2.blockY).sorted()
        val (minZ, maxZ) = listOf(pos1.blockZ, pos2.blockZ).sorted()

        return loc.blockX in minX..maxX &&
                loc.blockY in minY..maxY &&
                loc.blockZ in minZ..maxZ
    }
}