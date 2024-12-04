package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.StringUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadCacheCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        CacheConfig.reload()

        return true
    }
}