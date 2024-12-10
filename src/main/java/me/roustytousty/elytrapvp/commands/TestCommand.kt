package me.roustytousty.elytrapvp.commands

import me.roustytousty.elytrapvp.services.bounty.BountyService
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        BountyService().applyBounty(player, 334)

        return true
    }
}