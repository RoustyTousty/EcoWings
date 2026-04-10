package me.roustytousty.elytrapvp.listeners.perks

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.perk.PerkType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerVelocityEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class PerkListener : Listener {

    // KINETIC SHIELD & BLAST DAMPENER
    @EventHandler
    fun onPlayerDamage(e: EntityDamageEvent) {
        val player = e.entity as? Player ?: return

        when (e.cause) {
            EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
            EntityDamageEvent.DamageCause.ENTITY_EXPLOSION -> {
                if (Services.perkService.hasPerkEquipped(player, PerkType.BLAST_DAMPENER)) {
                    e.damage *= 0.2
                }
            }

            EntityDamageEvent.DamageCause.FLY_INTO_WALL -> {
                if (Services.perkService.hasPerkEquipped(player, PerkType.KINETIC_SHIELD)) {
                    e.damage *= 0.2
                }
            }
            else -> return
        }
    }

    // ADRENALINE
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        val victim = e.entity
        val killer = victim.killer ?: return

        if (Services.perkService.hasPerkEquipped(killer, PerkType.ADRENALINE)) {
            killer.addPotionEffect(PotionEffect(PotionEffectType.SPEED, 200, 1))
        }
    }

    // RECYCLER
    @EventHandler
    fun onBlockBreak(e: BlockBreakEvent) {
        val player = e.player
        val material = e.block.type

        if (Services.perkService.hasPerkEquipped(player, PerkType.RECYCLER)) {
            if (Services.shopService.isItemInShopType(material, "blocks")) {
                if (Random.nextDouble() <= 0.5) {
                    val shopItem = Services.shopService.getFormattedItem(material, 1)
                    player.inventory.addItem(shopItem)
                }
            }
        }
    }

    // SCAVENGER
    @EventHandler(ignoreCancelled = true)
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (e.hand != EquipmentSlot.HAND) return

        val player = e.player
        val material = e.block.type

        if (Services.perkService.hasPerkEquipped(player, PerkType.SCAVENGER)) {
            if (Services.shopService.isItemInShopType(material, "blocks")) {
                if (Random.nextDouble() <= 0.2) {
                    val plugin = JavaPlugin.getProvidingPlugin(PerkListener::class.java)
                    Bukkit.getScheduler().runTask(plugin, Runnable {
                        val refill = Services.shopService.getFormattedItem(material, 1)
                        player.inventory.addItem(refill)
                    })
                }
            }
        }
    }

    // ANCHORED
    @EventHandler
    fun onPlayerVelocity(e: PlayerVelocityEvent) {
        val player = e.player
        if (Services.perkService.hasPerkEquipped(player, PerkType.ANCHORED)) {
            val velocity = e.velocity
            velocity.x *= 0.90
            velocity.y *= 0.90
            velocity.z *= 0.90
            e.velocity = velocity
        }
    }
}