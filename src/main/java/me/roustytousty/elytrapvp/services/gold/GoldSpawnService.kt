package me.roustytousty.elytrapvp.services.gold

import me.roustytousty.elytrapvp.services.region.RegionService
import org.bukkit.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class GoldSpawnService(
    private val regionService: RegionService,
    private val plugin: JavaPlugin
) {

    private val activeGoldBlocks = mutableSetOf<Location>()

    private val maxBlocks = 3
    private val region = "goldSpawnRegion"

    init {
        cleanupRegion()
        startSpawner()
        startEffects()
    }

    fun isGoldBlock(loc: Location): Boolean {
        return activeGoldBlocks.any {
            it.blockX == loc.blockX && it.blockY == loc.blockY && it.blockZ == loc.blockZ
        }
    }

    fun removeGoldBlock(loc: Location) {
        activeGoldBlocks.removeIf {
            it.blockX == loc.blockX && it.blockY == loc.blockY && it.blockZ == loc.blockZ
        }
    }

    fun shutdown() {
        for (loc in activeGoldBlocks) {
            val block = loc.block
            if (block.type == Material.RAW_GOLD_BLOCK) {
                block.type = Material.AIR
            }
        }
        activeGoldBlocks.clear()
    }

    private fun startSpawner() {
        object : BukkitRunnable() {
            override fun run() {
                if (activeGoldBlocks.size >= maxBlocks) return

                repeat(maxBlocks - activeGoldBlocks.size) {
                    attemptSpawn()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 30)
    }

    private fun attemptSpawn() {
        val region = regionService.get(region) ?: return
        val (pos1, pos2) = region

        val world = pos1.world ?: return

        repeat(10) {
            val x = Random.nextInt(minOf(pos1.blockX, pos2.blockX), maxOf(pos1.blockX, pos2.blockX) + 1)
            val y = Random.nextInt(minOf(pos1.blockY, pos2.blockY), maxOf(pos1.blockY, pos2.blockY) + 1)
            val z = Random.nextInt(minOf(pos1.blockZ, pos2.blockZ), maxOf(pos1.blockZ, pos2.blockZ) + 1)

            val loc = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
            val block = loc.block

            if (!block.type.isAir) return@repeat

            block.type = Material.RAW_GOLD_BLOCK
            activeGoldBlocks.add(loc)
            return
        }
    }

    private fun startEffects() {
        object : BukkitRunnable() {
            override fun run() {
                val iterator = activeGoldBlocks.iterator()

                while (iterator.hasNext()) {
                    val loc = iterator.next()
                    val block = loc.block

                    if (block.type != Material.RAW_GOLD_BLOCK) {
                        iterator.remove()
                        continue
                    }

                    val world = loc.world ?: continue

                    world.spawnParticle(
                        Particle.END_ROD,
                        loc.clone().add(0.5, 0.5, 0.5),
                        5,
                        0.4, 0.4, 0.4,
                        0.01
                    )

                    world.spawnParticle(
                        Particle.GLOW,
                        loc.clone().add(0.5, 0.5, 0.5),
                        3,
                        0.2, 0.2, 0.2,
                        0.0
                    )

                    world.playSound(
                        loc,
                        Sound.BLOCK_BEACON_AMBIENT,
                        1.5f,
                        1.5f
                    )
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 2)
    }

    private fun cleanupRegion() {
        val regionPair = regionService.get(region) ?: return
        val (pos1, pos2) = regionPair

        val world = pos1.world ?: return

        val minX = minOf(pos1.blockX, pos2.blockX)
        val maxX = maxOf(pos1.blockX, pos2.blockX)

        val minY = minOf(pos1.blockY, pos2.blockY)
        val maxY = maxOf(pos1.blockY, pos2.blockY)

        val minZ = minOf(pos1.blockZ, pos2.blockZ)
        val maxZ = maxOf(pos1.blockZ, pos2.blockZ)

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    val block = world.getBlockAt(x, y, z)

                    if (block.type == Material.RAW_GOLD_BLOCK) {
                        block.type = Material.AIR
                    }
                }
            }
        }
    }
}