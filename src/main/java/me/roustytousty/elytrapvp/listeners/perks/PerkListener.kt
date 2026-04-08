package me.roustytousty.elytrapvp.listeners.perks

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.perk.PerkType
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.player.PlayerVelocityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.random.Random

class PerkListener : Listener {

    private val recyclableMaterials = setOf(
        Material.WHITE_WOOL, Material.LIGHT_GRAY_WOOL, Material.OAK_PLANKS,
        Material.STONE_BRICKS, Material.DEEPSLATE_BRICKS, Material.POLISHED_DEEPSLATE
    )

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
        if (Services.perkService.hasPerkEquipped(player, PerkType.RECYCLER)) {
            if (recyclableMaterials.contains(e.block.type)) {
                if (Random.nextDouble() <= 0.5) {
                    player.inventory.addItem(ItemStack(e.block.type, 1))
                }
            }
        }
    }

    // SCAVENGER
    @EventHandler(ignoreCancelled = true)
    fun onBlockPlace(e: BlockPlaceEvent) {
        val player = e.player
        if (Services.perkService.hasPerkEquipped(player, PerkType.SCAVENGER)) {
            if (Random.nextDouble() <= 0.2) {
                val item = e.itemInHand.clone()
                item.amount = 1
                player.inventory.addItem(item)
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