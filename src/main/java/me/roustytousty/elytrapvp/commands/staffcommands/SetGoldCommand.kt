package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetGoldCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (args.size != 2) {
            MessageUtils.sendError(player, "&6/setgold &f<player> <number>")
            return false
        }

        val selectedPlayer = Bukkit.getOfflinePlayer(args[0])
        val goldAmount = args[1].toInt()
        CacheConfig.setplrVal(selectedPlayer, "gold", goldAmount)
        MessageUtils.sendMessage(player, "&fSuccessfully set &6${selectedPlayer.name} &fgold to &6$goldAmount")

        return true
    }
}