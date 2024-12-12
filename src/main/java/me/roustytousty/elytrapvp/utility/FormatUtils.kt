package me.roustytousty.elytrapvp.utility

import org.bukkit.ChatColor
import java.text.DecimalFormat


object FormatUtils {

    fun parse(s: String?): String {
        return ChatColor.translateAlternateColorCodes('&', s!!)
    }

    fun formatNumber(number: Int): String {
        return when {
            number >= 1_000 -> {
                DecimalFormat("#.#k").format(number / 1_000.0)
            }
            else -> number.toString()
        }
    }
}