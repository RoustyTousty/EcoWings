package me.roustytousty.elytrapvp.commands

import me.roustytousty.elytrapvp.gui.events.EventsMenu
import me.roustytousty.elytrapvp.gui.perks.PerksMenu
import me.roustytousty.elytrapvp.gui.shops.ShopMenu
import me.roustytousty.elytrapvp.gui.stats.StatsMenu
import me.roustytousty.elytrapvp.gui.upgrade.UpgradeMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GuiCommands : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (command.name.equals("upgrademenu", ignoreCase = true)) {
            UpgradeMenu.openInventory(player)
        } else if (command.name.equals("shopmenu", ignoreCase = true)) {
            ShopMenu.openInventory(player)
        } else if (command.name.equals("statsmenu", ignoreCase = true)) {
            StatsMenu.openInventory(player)
        } else if (command.name.equals("perksmenu", ignoreCase = true)) {
            PerksMenu.openInventory(player)
        } else if (command.name.equals("eventsmenu", ignoreCase = true)) {
            EventsMenu.openInventory(player)
        }
        return true
    }
}