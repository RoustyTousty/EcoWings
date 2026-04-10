package me.roustytousty.elytrapvp.listeners

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent
import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class OnPlayerPostRespawn : Listener {

    private val kitService = Services.kitService
    private val combatService = Services.combatService

    @EventHandler
    fun onPlayerPostRespawn(event: PlayerPostRespawnEvent) {
        val player = event.player

        kitService.syncKit(player)

        combatService.applyRespawnProtection(player)

        val hiddenResistance = PotionEffect(
            PotionEffectType.DAMAGE_RESISTANCE,
            300,
            0,
            false,
            false,
            false
        )

        player.addPotionEffect(hiddenResistance)

        MessageUtils.sendTitle(player, "&c&lDeath", "&7Try harder next time.", 5, 30, 5)
    }
}