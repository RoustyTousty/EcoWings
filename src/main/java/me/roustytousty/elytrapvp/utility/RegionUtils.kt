package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.data.RegionConfig
import org.bukkit.Location
import org.bukkit.Material

object RegionUtils {

    /*
        Check if a location is inside a region
     */
    fun isLocationInRegion(location: Location, regionName: String): Boolean {
        val (pos1, pos2) = RegionConfig.getRegionPositions(regionName)!!

        if (location.world != pos1.world || location.world != pos2.world) return false

        val (minX, maxX) = listOf(pos1.x, pos2.x).sorted()
        val (minY, maxY) = listOf(pos1.y, pos2.y).sorted()
        val (minZ, maxZ) = listOf(pos1.z, pos2.z).sorted()

        return location.x in minX..maxX && location.y in minY..maxY && location.z in minZ..maxZ
    }



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