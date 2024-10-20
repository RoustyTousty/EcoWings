package me.roustytousty.elytrapvp.commands

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
        if (command.name.equals("upgrade", ignoreCase = true)) {
            UpgradeMenu.openInventory(player)
        } else if (command.name.equals("shop", ignoreCase = true)) {
            ShopMenu.openInventory(player)
        } else if (command.name.equals("stats", ignoreCase = true)) {
            StatsMenu.openInventory(player)
        } else if (command.name.equals("perks", ignoreCase = true)) {
            PerksMenu.openInventory(player)
        }
        return true
    }
}