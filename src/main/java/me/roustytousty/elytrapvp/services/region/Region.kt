package me.roustytousty.elytrapvp.services.region

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import kotlin.random.Random

class Region(
    val world: World,
    private val minX: Int,
    private val maxX: Int,
    private val minY: Int,
    private val maxY: Int,
    private val minZ: Int,
    private val maxZ: Int
) {

    fun contains(loc: Location): Boolean {
        if (loc.world != world) return false

        return loc.blockX in minX..maxX &&
                loc.blockY in minY..maxY &&
                loc.blockZ in minZ..maxZ
    }

    fun forEachBlock(action: (Block) -> Unit) {
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    action(world.getBlockAt(x, y, z))
                }
            }
        }
    }

    fun getBlocks(filter: (Block) -> Boolean): List<Block> {
        val result = mutableListOf<Block>()
        forEachBlock {
            if (filter(it)) result.add(it)
        }
        return result
    }

    fun clear() {
        forEachBlock {
            it.type = Material.AIR
        }
    }

    fun getRandomLocation(): Location {
        val x = Random.nextInt(minX, maxX + 1)
        val y = Random.nextInt(minY, maxY + 1)
        val z = Random.nextInt(minZ, maxZ + 1)
        return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
    }

    fun getMinY(): Int = minY
    fun getMaxY(): Int = maxY
    fun getMinX(): Int = minX
    fun getMaxX(): Int = maxX
    fun getMinZ(): Int = minZ
    fun getMaxZ(): Int = maxZ
}