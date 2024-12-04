package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.StringUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BuildModeCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        if (!player.isOp) {
            player.sendMessage(StringUtils.parse("&c&lWwwings &8| &fYou don't have permission to use this command!"))
            return false
        }

        val buildmode = CacheConfig.getplrVal(player, "isBuildMode")
        if (buildmode == true) {
            CacheConfig.setplrVal(player, "isBuildMode", false)
            player.sendMessage(StringUtils.parse("&6&lWwwings &8| &6Buildmode &fis now &c&lOFF&f!"))
        } else {
            CacheConfig.setplrVal(player, "isBuildMode", true)
            player.sendMessage(StringUtils.parse("&6&lWwwings &8| &6Buildmode &fis now &a&lON&f!"))
        }

        return true
    }
}