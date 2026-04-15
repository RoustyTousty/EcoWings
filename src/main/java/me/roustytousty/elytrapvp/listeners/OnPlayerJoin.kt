package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class OnPlayerJoin : Listener {

    private val playerService = Services.playerService
    private val scoreboardService = Services.scoreboardService
    private val kitService = Services.kitService

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        playerService.getOrCreatePlayerData(player)

        player.teleport(Location(Bukkit.getWorld("EcoWings"), 0.0, 137.0, 175.0, -180.0F, 0.0F))
        event.joinMessage(Component.text(parse("&f[&a+&f] ${player.name}")))

        kitService.syncKit(player)
        scoreboardService.create(player)

        Bukkit.getOnlinePlayers().forEach { onlinePlayer ->
            scoreboardService.updateNameTag(onlinePlayer)
        }

        val nightvision = PotionEffect(
            PotionEffectType.NIGHT_VISION,
            1000000,
            0,
            false,
            false,
            false
        )

        player.addPotionEffect(nightvision)
    }
}