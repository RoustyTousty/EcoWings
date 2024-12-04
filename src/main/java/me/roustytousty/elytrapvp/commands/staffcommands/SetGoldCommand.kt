package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.StringUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetGoldCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (args.size != 2) {
            player.sendMessage(StringUtils.parse("&c&lWwwings &8| &6/setgold &f<player> <number>"))
            return false
        }

        val selectedPlayer = Bukkit.getOfflinePlayer(args[0])
        val goldAmount = args[1].toInt()
        CacheConfig.setplrVal(selectedPlayer, "gold", goldAmount)
        player.sendMessage(StringUtils.parse("&a&lWwwings &8| &fSuccessfully set &6${selectedPlayer.name} &fgold to &6$goldAmount"))

        return true
    }
}