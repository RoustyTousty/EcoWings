package me.roustytousty.elytrapvp.commands.playercommands

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class SpawnCommand : CommandExecutor {

    private val combatService = Services.combatService
    private val plugin = Services.plugin

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) return true

        val player = sender
        if (combatService.isInCombat(player)) {
            MessageUtils.sendError(player, "&fCan't go to spawn while in combat!")
            return false
        }

        val startLoc = player.location.block.location

        object : BukkitRunnable() {
            var secondsLeft = 3

            override fun run() {
                if (!player.isOnline) {
                    cancel()
                    return
                }

                if (player.location.block.location != startLoc) {
                    MessageUtils.sendError(player, "&fTeleport cancelled! You moved.")
                    SoundUtils.playFailure(player)
                    cancel()
                    return
                }

                if (combatService.isInCombat(player)) {
                    MessageUtils.sendError(player, "&fTeleport cancelled! You entered combat.")
                    SoundUtils.playFailure(player)
                    cancel()
                    return
                }

                if (secondsLeft > 0) {
                    MessageUtils.sendMessage(player, "&fTeleporting in &6$secondsLeft&f...")
                    SoundUtils.playGuiClick(player)
                    secondsLeft--
                } else {
                    player.teleport(Location(Bukkit.getWorld("EcoWings"), 0.0, 137.0, 175.0, -180.0F, 0.0F))
                    MessageUtils.sendMessage(player, "&fTeleported to spawn!")
                    SoundUtils.playSuccess(player)
                    cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)

        return true
    }
}