package me.roustytousty.elytrapvp.services.mapreset

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.region.RegionService
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.scheduler.BukkitRunnable

class MapResetService(
    private val regionService: RegionService
) {

    private val RESET_INTERVAL_SECONDS = 60 * 60
    private val REGION_NAME = "pvpRegion"

    init {
        ResetTask().runTaskTimer(ElytraPVP.instance!!, 0L, 20L)
    }

    private var timeRemainingSeconds = RESET_INTERVAL_SECONDS

    fun getFormattedTimeRemaining(): String {
        if (timeRemainingSeconds <= 0) return "00:00"

        val minutes = timeRemainingSeconds / 60
        val seconds = timeRemainingSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun resetMapRegion() {
        regionService.clearRegion(REGION_NAME)
    }

    private inner class ResetTask : BukkitRunnable() {

        override fun run() {
            when (timeRemainingSeconds) {
                60 -> MessageUtils.sendMessage("&fMap will reset in &6&l1 &fminute!")
                10, 3, 2, 1 -> MessageUtils.sendMessage("&fMap will reset in &6&l$timeRemainingSeconds &fsecond${if (timeRemainingSeconds > 1) "s" else ""}!")
                0 -> {
                    resetMapRegion()
                    resetTask()
                }
            }
            timeRemainingSeconds--
        }

        private fun resetTask() {
            timeRemainingSeconds = RESET_INTERVAL_SECONDS
        }
    }
}