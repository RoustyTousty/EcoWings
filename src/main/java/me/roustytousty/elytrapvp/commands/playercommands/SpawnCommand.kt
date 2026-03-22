package me.roustytousty.elytrapvp.commands.playercommands

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnCommand : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        val isInCombat = Services.combatService.isInCombat(player)

        if (isInCombat) {
            MessageUtils.sendError(player, "&fCan't go to spawn while in combat!")
            return false
        }

        player.teleport(Location(Bukkit.getWorld("EcoWings"), 0.0, 137.0, 175.0, -180.0F, 0.0F))
        MessageUtils.sendMessage(player, "&fTeleported to spawn!")

        return true
    }
}