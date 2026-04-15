package me.roustytousty.elytrapvp.listeners

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class OnPlayerPostRespawn : Listener {

    private val kitService = Services.kitService
    private val combatService = Services.combatService
    private val plugin = Services.plugin

    @EventHandler
    fun onPlayerPostRespawn(event: PlayerPostRespawnEvent) {
        Bukkit.getScheduler().runTask(plugin, Runnable {
            val player = event.player

            kitService.syncKit(player)
            combatService.applyRespawnProtection(player)

            val nightvision = PotionEffect(
                PotionEffectType.NIGHT_VISION,
                -1,
                0,
                false,
                false,
                false
            )

            val strenght = PotionEffect(
                PotionEffectType.INCREASE_DAMAGE,
                200,
                0,
                false,
                false,
                false
            )

            val resistance = PotionEffect(
                PotionEffectType.DAMAGE_RESISTANCE,
                300,
                1,
                false,
                false,
                false
            )

            player.addPotionEffect(nightvision)
            player.addPotionEffect(resistance)
            player.addPotionEffect(strenght)

            MessageUtils.sendTitle(
                player,
                "&c&lDeath",
                "&7Try harder next time.",
                5,
                30,
                5
            )
        })
    }
}