package me.roustytousty.elytrapvp.commands.staffcommands

import me.roustytousty.elytrapvp.ElytraPVP
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class EventActivateCommand : CommandExecutor {

    private val eventService = ElytraPVP.instance!!.getEventService()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {

        eventService.activateEvent("Voidless")

        return true
    }
}