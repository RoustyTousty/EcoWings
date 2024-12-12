package me.roustytousty.elytrapvp.commands.playercommands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GoldCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        val gold = CacheConfig.getplrVal(player, "gold")
        MessageUtils.sendMessage(player, "&fGold: &6${gold}g")

        return true
    }
}