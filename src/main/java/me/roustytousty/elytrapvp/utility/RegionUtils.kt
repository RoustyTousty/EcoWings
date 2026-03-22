package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.data.configs.RegionConfig
import org.bukkit.Location
import org.bukkit.Material

object RegionUtils {

    /*
        Clears a specific region
     */
    fun resetRegion(regionName: String) {
        val (pos1, pos2) = RegionConfig.getRegionPositions(regionName)!!

        if (pos1 != null && pos2 != null) {
            clearRegion(pos1, pos2)
        }
    }

    private fun clearRegion(pos1: Location, pos2: Location) {
        val world = pos1.world ?: return

        val (minX, maxX) = listOf(pos1.blockX, pos2.blockX).sorted()
        val (minY, maxY) = listOf(pos1.blockY, pos2.blockY).sorted()
        val (minZ, maxZ) = listOf(pos1.blockZ, pos2.blockZ).sorted()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    world.getBlockAt(x, y, z).type = Material.AIR
                }
            }
        }
    }
}