package me.roustytousty.elytrapvp.utility

import org.bukkit.ChatColor
import java.text.DecimalFormat


object FormatUtils {



    /*
        Formats a string to the official minecraft color codes
     */
    fun parse(s: String?): String {
        return ChatColor.translateAlternateColorCodes('&', s!!)
    }



    /*
        Formats numbers
     */
    fun formatNumber(number: Int): String {
        return when {
            number >= 1_000 -> {
                DecimalFormat("#.#k").format(number / 1_000.0)
            }
            else -> number.toString()
        }
    }



    /*
        Formats time duration
     */
    fun formatDuration(millis: Long): String {
        if (millis == Long.MAX_VALUE || millis == -1L) return "Permanent"
        if (millis <= 0) return "0s"

        val seconds = millis / 1000
        val d = seconds / 86400
        val h = (seconds % 86400) / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60

        val builder = StringBuilder()
        if (d > 0) builder.append("${d}d ")
        if (h > 0) builder.append("${h}h ")
        if (m > 0) builder.append("${m}m ")
        if (s > 0 && d == 0L) builder.append("${s}s")

        return builder.toString().trim()
    }
}