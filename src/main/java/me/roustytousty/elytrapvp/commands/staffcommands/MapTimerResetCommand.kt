package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MapTimerResetCommand : CommandExecutor {

    private val mapService = Services.mapService

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player

        mapService.resetMapTimer()

        MessageUtils.sendMessage(player, "&fMap timer has been reset to the default value!")

        return true
    }
}