package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.RegionConfig
import me.roustytousty.elytrapvp.utility.StringUtils.parse
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

object RegionUtils {

    private const val RESET_INTERVAL_MINUTES = 1
    private const val FINAL_COUNTDOWN_SECONDS = 60
    private const val REGION_NAME = "pvpRegion"

    fun startRegionResetTask() {

        object : BukkitRunnable() {
            var timeRemainingMinutes = RESET_INTERVAL_MINUTES
            var finalCountdownSeconds = FINAL_COUNTDOWN_SECONDS
            var inFinalCountdown = false

            override fun run() {
                if (inFinalCountdown) {
                    handleFinalCountdown()
                    return
                }

                if (timeRemainingMinutes == 1) {
                    sendWarningToPlayers("&6&lEcoWings &8| &fMap will reset in 1 minute!")
                }

                if (timeRemainingMinutes == 0) {
                    inFinalCountdown = true
                    return
                }

                timeRemainingMinutes--
            }

            private fun handleFinalCountdown() {
                when (finalCountdownSeconds) {
                    10, 3, 2, 1 -> sendWarningToPlayers("&6&lEcoWings &8| &fMap will reset in $finalCountdownSeconds second${if (finalCountdownSeconds > 1) "s" else ""}!")
                    0 -> {


                        val pos1 = RegionConfig.getRegionPos1(REGION_NAME)
                        val pos2 = RegionConfig.getRegionPos2(REGION_NAME)

                        if (pos1 != null && pos2 != null) {
                            clearRegion(pos1, pos2)
                        }
                        timeRemainingMinutes = RESET_INTERVAL_MINUTES
                        finalCountdownSeconds = FINAL_COUNTDOWN_SECONDS
                        inFinalCountdown = false
                    }
                }

                finalCountdownSeconds--
            }
        }.runTaskTimer(ElytraPVP.instance!!, 0L, 20L)
    }

    private fun clearRegion(pos1: Location, pos2: Location) {
        val world: World = pos1.world ?: return
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
                    block.type = Material.AIR
                }
            }
        }
    }

    private fun sendWarningToPlayers(message: String) {
        for (player: Player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(parse(message))
        }
    }

    fun isLocationInRegion(location: Location, regionName: String): Boolean {
        val pos1 = RegionConfig.getRegionPos1(regionName) ?: return false
        val pos2 = RegionConfig.getRegionPos2(regionName) ?: return false

        if (location.world != pos1.world || location.world != pos2.world) return false

        val minX = Math.min(pos1.x, pos2.x)
        val minY = Math.min(pos1.y, pos2.y)
        val minZ = Math.min(pos1.z, pos2.z)

        val maxX = Math.max(pos1.x, pos2.x)
        val maxY = Math.max(pos1.y, pos2.y)
        val maxZ = Math.max(pos1.z, pos2.z)

        return location.x in minX..maxX && location.y in minY..maxY && location.z in minZ..maxZ
    }
}