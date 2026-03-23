package me.roustytousty.elytrapvp.services.region

import me.roustytousty.elytrapvp.data.configs.RegionConfig
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block

class RegionService {

    private val regions = mutableMapOf<String, Region>()

    init {
        load()
    }

    fun load() {
        val config = RegionConfig.getConfig()
        val section = config.getConfigurationSection("regions") ?: return

        for (key in section.getKeys(false)) {
            val base = "regions.$key"

            val worldName = config.getString("$base.pos1.world") ?: continue
            val world = Bukkit.getWorld(worldName) ?: continue

            val x1 = config.getInt("$base.pos1.x")
            val y1 = config.getInt("$base.pos1.y")
            val z1 = config.getInt("$base.pos1.z")

            val x2 = config.getInt("$base.pos2.x")
            val y2 = config.getInt("$base.pos2.y")
            val z2 = config.getInt("$base.pos2.z")

            val minX = minOf(x1, x2)
            val maxX = maxOf(x1, x2)
            val minY = minOf(y1, y2)
            val maxY = maxOf(y1, y2)
            val minZ = minOf(z1, z2)
            val maxZ = maxOf(z1, z2)

            regions[key] = Region(world, minX, maxX, minY, maxY, minZ, maxZ)
        }
    }

    fun get(name: String): Region? = regions[name]

    fun isInRegion(loc: Location, name: String): Boolean {
        return regions[name]?.contains(loc) ?: false
    }

    fun forEachBlock(name: String, action: (Block) -> Unit) {
        regions[name]?.forEachBlock(action)
    }

    fun clearRegion(name: String) {
        regions[name]?.clear()
    }

    fun getBlocks(
        name: String,
        filter: (Block) -> Boolean
    ): List<Block> {
        return regions[name]?.getBlocks(filter) ?: emptyList()
    }
}