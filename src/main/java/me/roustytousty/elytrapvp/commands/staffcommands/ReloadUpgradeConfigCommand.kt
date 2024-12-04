package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.data.UpgradeConfig
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadUpgradeConfigCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        UpgradeConfig.load()

        return true
    }
}