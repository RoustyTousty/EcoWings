package me.roustytousty.elytrapvp.listeners.items.consumables

import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class SpeedFeather : Listener {

    @EventHandler
    fun onDusty(event: PlayerInteractEvent) {
        if (event.action.name.contains("RIGHT_CLICK")) {

            val player = event.player
            val item = player.inventory.itemInMainHand

            if (item.type == Material.FEATHER) {
                player.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 20 * 30, 0, false, false))
                player.playSound(player, Sound.ENTITY_GENERIC_EAT, 1.0f, 1.0f)
                item.amount -= 1
            }
        }
    }
}