package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object MessageUtils {

    fun sendError(player: Player, message: String) {
        player.sendMessage(parse("&c&lEcoWings &8| $message"))
    }

    fun sendError(message: String) {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendMessage(parse("&c&lEcoWings &8| $message"))
        }
    }

    fun sendSuccess(player: Player, message: String) {
        player.sendMessage(parse("&a&lEcoWings &8| $message"))
    }

    fun sendSuccess(message: String) {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendMessage(parse("&a&lEcoWings &8| $message"))
        }
    }

    fun sendMessage(player: Player, message: String) {
        player.sendMessage(parse("&6&lEcoWings &8| $message"))
    }

    fun sendMessage(message: String) {
        for (player: Player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(parse("&6&lEcoWings &8| $message"))
        }
    }

    fun sendActionBar(player: Player, actionBar: String) {
        player.sendActionBar(parse(actionBar))
    }

    fun sendActionBar(actionBar: String) {
        for (player: Player in Bukkit.getOnlinePlayers()) {
            player.sendActionBar(parse(actionBar))
        }
    }

    fun sendTitle(player: Player, title: String, subTitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        player.sendTitle(parse(title), parse(subTitle), fadeIn, stay, fadeOut)
    }

    fun sendTitle(title: String, subTitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        for (player: Player in Bukkit.getOnlinePlayers()) {
            player.sendTitle(parse(title), parse(subTitle), fadeIn, stay, fadeOut)
        }
    }
}