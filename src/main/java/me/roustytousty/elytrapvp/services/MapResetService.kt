package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.scheduler.BukkitRunnable

class MapResetService {

    companion object {
        private const val RESET_INTERVAL_SECONDS = 3 * 60
        private const val REGION_NAME = "pvpRegion"
    }

    fun startRegionResetTask() {
        ResetTask().runTaskTimer(ElytraPVP.instance!!, 0L, 20L)
    }

    private inner class ResetTask : BukkitRunnable() {

        private var timeRemainingSeconds = RESET_INTERVAL_SECONDS
        private var inFinalCountdown = false

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
            inFinalCountdown = false
        }
    }
}