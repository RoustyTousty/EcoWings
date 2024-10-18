package me.roustytousty.elytrapvp.commands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import me.roustytousty.elytrapvp.utility.StringUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StaffCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (!player.isOp) {
            player.sendMessage(StringUtils.parse("&c&lWwwings &8| &fYou don't have permission to use this command!"))
            return false
        }

        if (command.name.equals("reloadcache", ignoreCase = true)) {
            CacheConfig.reload()
        } else if (command.name.equals("reloadupgradeconfig", ignoreCase = true)) {
            UpgradeConfig.load()
        } else if (command.name.equals("feed", ignoreCase = true)) {
            player.foodLevel = 20
        } else if (command.name.equals("buildmode", ignoreCase = true)) {
            val buildmode = CacheConfig.getplrVal(player, "isBuildMode")
            if (buildmode == true) {
                CacheConfig.setplrVal(player, "isBuildMode", false)
                player.sendMessage(StringUtils.parse("&6&lWwwings &8| &6Buildmode &fis now &c&lOFF&f!"))
            } else {
                CacheConfig.setplrVal(player, "isBuildMode", true)
                player.sendMessage(StringUtils.parse("&6&lWwwings &8| &6Buildmode &fis now &a&lON&f!"))
            }
        } else if (command.name.equals("setgold", ignoreCase = true)) {
            if (args.size != 2) {
                player.sendMessage(StringUtils.parse("&c&lWwwings &8| &6/setgold &f<player> <number>"))
                return false
            }
            val selectedPlayer = Bukkit.getOfflinePlayer(args[0])
            val goldAmount = args[1].toInt()
            CacheConfig.setplrVal(selectedPlayer, "gold", goldAmount)
            player.sendMessage(StringUtils.parse("&a&lWwwings &8| &fSuccessfully set &6${selectedPlayer.name} &fgold to &6$goldAmount"))
        } else if (command.name.equals("givegold", ignoreCase = true)) {

        }
        return true
    }
}