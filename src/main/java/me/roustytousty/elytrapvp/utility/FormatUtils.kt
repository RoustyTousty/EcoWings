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
}