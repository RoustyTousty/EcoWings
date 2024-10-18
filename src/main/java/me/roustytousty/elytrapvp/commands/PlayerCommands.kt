package me.roustytousty.elytrapvp.commands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.StringUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PlayerCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (command.name.equals("gold", ignoreCase = true)) {
            val gold = CacheConfig.getplrVal(player, "gold")
            player.sendMessage(StringUtils.parse("&6&lWwwings &8| &fGold: &6${gold}g"))
        }
        return true
    }
}