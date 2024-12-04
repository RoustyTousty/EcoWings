package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.CacheConfig
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadCacheCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        CacheConfig.reload()

        return true
    }
}