package me.roustytousty.elytrapvp.services.gold

import me.roustytousty.elytrapvp.services.region.RegionService
import org.bukkit.*
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class GoldSpawnService(
    private val regionService: RegionService,
    private val plugin: JavaPlugin
) {

    private val activeGoldBlocks = mutableSetOf<Location>()

    private val MAX_BLOCKS = 6
    private val MIN_BLOCKS = 3
    private val PLAYER_COUNT_INTERVAL = 5
    private val SPAWN_DELAY_SECONDS = 30
    private val SPAWN_ATTEMPTS = 10

    private val BUILD_BUFFER_REGION = "buildBufferRegion"
    private val REGION = "goldSpawnRegion"

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
                val currentPlayers = Bukkit.getOnlinePlayers().size
                val targetBlocks = minOf(MIN_BLOCKS + (currentPlayers / PLAYER_COUNT_INTERVAL), MAX_BLOCKS)

                if (activeGoldBlocks.size < targetBlocks) {
                    attemptSpawn()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * SPAWN_DELAY_SECONDS)
    }

    private fun attemptSpawn() {
        val regionObj = regionService.get(REGION) ?: return

        repeat(SPAWN_ATTEMPTS) {
            val loc = regionObj.getRandomLocation()
            val block = loc.block

            if (block.type.isAir && !regionService.isInRegion(loc, BUILD_BUFFER_REGION)) {
                block.type = Material.RAW_GOLD_BLOCK
                activeGoldBlocks.add(loc)
                return
            }
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
        regionService.forEachBlock(REGION) {
            if (it.type == Material.RAW_GOLD_BLOCK) {
                it.type = Material.AIR
            }
        }
    }
}