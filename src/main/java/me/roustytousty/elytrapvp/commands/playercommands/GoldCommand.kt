package me.roustytousty.elytrapvp.commands.playercommands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.StringUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GoldCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        val gold = CacheConfig.getplrVal(player, "gold")
        player.sendMessage(StringUtils.parse("&6&lEcoWings &8| &fGold: &6${gold}g"))

        return true
    }
}