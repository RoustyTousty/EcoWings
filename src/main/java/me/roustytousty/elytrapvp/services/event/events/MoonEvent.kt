package me.roustytousty.elytrapvp.services.event.events

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.event.EventIntefrace
import net.citizensnpcs.api.CitizensAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class MoonEvent : EventIntefrace {
    override val name = "Moon"
    override val description = "Time to float!"
    override val displayMaterial = Material.END_STONE
    override val cost = 220
    override var contributions = 0
    override val duration = 5 * 60
    override var endTime: Long? = null
    override var task: BukkitTask? = null
    override var isActive = false

    private val KNOCKBACK_MULTIPLIER = 2.0
    private val JUMP_BOOST = PotionEffect(PotionEffectType.JUMP, Int.MAX_VALUE, 1, false, false, false)
    private val FEATHER_FALLING = PotionEffect(PotionEffectType.SLOW_FALLING, Int.MAX_VALUE, 0, false, false, false)

    private val listener = object : Listener {
        @EventHandler
        fun onPlayerDamageByEntity(event: EntityDamageByEntityEvent) {
            val player = event.entity
            if (player is Player && !CitizensAPI.getNPCRegistry().isNPC(player)) {
                val attacker = event.damager
                val direction = player.location.toVector().subtract(attacker.location.toVector()).normalize()
                val knockbackVector = direction.multiply(KNOCKBACK_MULTIPLIER).setY(0.5)
                player.velocity = knockbackVector
            }
        }

        @EventHandler
        fun onPlayerJoin(event: PlayerJoinEvent) {
            if (isActive) {
                applyEffects(event.player)
            }
        }

        @EventHandler
        fun onPlayerQuit(event: PlayerQuitEvent) {
            removeEffects(event.player)
        }

        @EventHandler
        fun onPlayerDeath(event: PlayerDeathEvent) {
            val player = event.entity
            if (isActive) {
                object : BukkitRunnable() {
                    override fun run() {
                        if (player.isOnline) {
                            applyEffects(player)
                        }
                    }
                }.runTaskLater(ElytraPVP.instance!!, 20L)
            }
        }
    }

    override fun activate() {
        isActive = true

        Bukkit.getOnlinePlayers().forEach { applyEffects(it) }

        Bukkit.getPluginManager().registerEvents(listener, ElytraPVP.instance!!)
    }

    override fun deactivate() {
        isActive = false

        Bukkit.getOnlinePlayers().forEach { removeEffects(it) }

        EntityDamageByEntityEvent.getHandlerList().unregister(listener)
        PlayerJoinEvent.getHandlerList().unregister(listener)
        PlayerQuitEvent.getHandlerList().unregister(listener)
        PlayerDeathEvent.getHandlerList().unregister(listener)
    }

    private fun applyEffects(player: Player) {
        player.addPotionEffect(JUMP_BOOST)
        player.addPotionEffect(FEATHER_FALLING)
    }

    private fun removeEffects(player: Player) {
        player.removePotionEffect(PotionEffectType.JUMP)
        player.removePotionEffect(PotionEffectType.SLOW_FALLING)
    }
}