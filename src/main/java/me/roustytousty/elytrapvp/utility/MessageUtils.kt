package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object MessageUtils {



    /*
        Sends a formatted error [message] to [player].
     */
    fun sendError(player: Player, message: String) {
        player.sendMessage(parse("&c&lEcoWings &8| $message"))
    }



    /*
        Sends a formatted error [message] to all players.
     */
    fun sendError(message: String) {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendMessage(parse("&c&lEcoWings &8| $message"))
        }
    }



    /*
        Sends a formatted success [message] to [player].
     */
    fun sendSuccess(player: Player, message: String) {
        player.sendMessage(parse("&a&lEcoWings &8| $message"))
    }



    /*
        Sends a formatted success [message] to all players.
     */
    fun sendSuccess(message: String) {
        Bukkit.getOnlinePlayers().forEach { player ->
            player.sendMessage(parse("&a&lEcoWings &8| $message"))
        }
    }



    /*
        Sends a formatted general [message] to [player].
     */
    fun sendMessage(player: Player, message: String) {
        player.sendMessage(parse("&6&lEcoWings &8| $message"))
    }



    /*
        Sends a formatted general [message] to all players.
     */
    fun sendMessage(message: String) {
        for (player: Player in Bukkit.getOnlinePlayers()) {
            player.sendMessage(parse("&6&lEcoWings &8| $message"))
        }
    }



    /*
        Sends a formatted action bar [message] to [player].
     */
    fun sendActionBar(player: Player, actionBar: String) {
        player.sendActionBar(parse(actionBar))
    }



    /*
        Sends a formatted action bar [message] to all players.
     */
    fun sendActionBar(actionBar: String) {
        for (player: Player in Bukkit.getOnlinePlayers()) {
            player.sendActionBar(parse(actionBar))
        }
    }



    /*
        Sends a formatted [title] with a [subTitle] to [player].
     */
    fun sendTitle(player: Player, title: String, subTitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        player.sendTitle(parse(title), parse(subTitle), fadeIn, stay, fadeOut)
    }



    /*
        Sends a formatted [title] with a [subTitle] to all players.
     */
    fun sendTitle(title: String, subTitle: String, fadeIn: Int, stay: Int, fadeOut: Int) {
        for (player: Player in Bukkit.getOnlinePlayers()) {
            player.sendTitle(parse(title), parse(subTitle), fadeIn, stay, fadeOut)
        }
    }
}