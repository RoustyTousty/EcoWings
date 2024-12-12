package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BuildModeCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        val buildmode = CacheConfig.getplrVal(player, "isBuildMode")
        if (buildmode == true) {
            CacheConfig.setplrVal(player, "isBuildMode", false)
            MessageUtils.sendMessage(player, "&6Buildmode &fis now &c&lOFF&f!")
        } else {
            CacheConfig.setplrVal(player, "isBuildMode", true)
            MessageUtils.sendMessage(player, "&6Buildmode &fis now &a&lON&f!")
        }

        return true
    }
}