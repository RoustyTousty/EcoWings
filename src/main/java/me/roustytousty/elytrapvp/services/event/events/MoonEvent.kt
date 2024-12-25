package me.roustytousty.elytrapvp.services.event.events

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.event.EventIntefrace
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent

class MoonEvent : EventIntefrace {
    override val name = "Knockback"
    override val description = "Time to fly!"
    override val cost = 220
    override var contributions = 0
    override val duration = 5 * 60
    override var isActive = false

    private val KNOCKBACK_MULTIPLIER = 2.0

    private val listener = object : Listener {
        @EventHandler
        fun onPlayerDamageByEntity(event: EntityDamageByEntityEvent) {
            val player = event.entity
            if (player is Player) {
                val attacker = event.damager
                val direction = player.location.toVector().subtract(attacker.location.toVector()).normalize()
                val knockbackVector = direction.multiply(KNOCKBACK_MULTIPLIER).setY(0.5)
                player.velocity = knockbackVector
            }
        }
    }

    override fun activate() {
        isActive = true
        Bukkit.getPluginManager().registerEvents(listener, ElytraPVP.instance!!)
    }

    override fun deactivate() {
        isActive = false
        EntityDamageByEntityEvent.getHandlerList().unregister(listener)
    }
}