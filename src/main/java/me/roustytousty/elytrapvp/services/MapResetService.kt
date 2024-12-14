package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.RegionUtils
import org.bukkit.scheduler.BukkitRunnable

class MapResetService {

    companion object {
        private const val RESET_INTERVAL_MINUTES = 3
        private const val FINAL_COUNTDOWN_SECONDS = 60
        private const val REGION_NAME = "pvpRegion"
    }

    fun startRegionResetTask() {
        ResetTask().runTaskTimer(ElytraPVP.instance!!, 0L, 20L)
    }

    private inner class ResetTask : BukkitRunnable() {

        private var timeRemainingMinutes = RESET_INTERVAL_MINUTES
        private var finalCountdownSeconds = FINAL_COUNTDOWN_SECONDS
        private var inFinalCountdown = false

        override fun run() {
            if (inFinalCountdown) {
                handleFinalCountdown()
            } else {
                handleIntervalCountdown()
            }
        }

        private fun handleIntervalCountdown() {
            if (timeRemainingMinutes == 1) {
                MessageUtils.sendMessage("&fMap will reset in 1 minute!")
                timeRemainingMinutes--
            } else if (timeRemainingMinutes == 0) {
                inFinalCountdown = true
            } else {
                timeRemainingMinutes--
            }
        }

        private fun handleFinalCountdown() {
            when (finalCountdownSeconds) {
                in 1..10 -> MessageUtils.sendMessage("&fMap will reset in $finalCountdownSeconds second${if (finalCountdownSeconds > 1) "s" else ""}!")
                0 -> {
                    RegionUtils.resetRegion(REGION_NAME)
                    resetTask()
                }
            }
            finalCountdownSeconds--
        }

        private fun resetTask() {
            timeRemainingMinutes = RESET_INTERVAL_MINUTES
            finalCountdownSeconds = FINAL_COUNTDOWN_SECONDS
            inFinalCountdown = false
        }
    }
}