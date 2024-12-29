package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.scheduler.BukkitRunnable

class MapResetService {

    companion object {
        private const val RESET_INTERVAL_SECONDS = 40 * 60
        private const val REGION_NAME = "pvpRegion"
    }

    fun startRegionResetTask() {
        ResetTask().runTaskTimer(ElytraPVP.instance!!, 0L, 20L)
    }

    private var timeRemainingSeconds = RESET_INTERVAL_SECONDS

    fun getFormattedTimeRemaining(): String {
        if (timeRemainingSeconds <= 0) return "00:00"

        val minutes = timeRemainingSeconds / 60
        val seconds = timeRemainingSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private inner class ResetTask : BukkitRunnable() {

        override fun run() {
            when (timeRemainingSeconds) {
                60 -> MessageUtils.sendMessage("&fMap will reset in &6&l1 &fminute!")
                10, 3, 2, 1 -> MessageUtils.sendMessage("&fMap will reset in &6&l$timeRemainingSeconds &fsecond${if (timeRemainingSeconds > 1) "s" else ""}!")
                0 -> {
                    RegionUtils.resetRegion(REGION_NAME)
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