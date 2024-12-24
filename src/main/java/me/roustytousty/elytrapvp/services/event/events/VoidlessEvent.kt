package me.roustytousty.elytrapvp.services.event.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.services.event.EventIntefrace
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class VoidlessEvent : EventIntefrace {
    override val name = "Void Block"
    override val description = "Prevents players from falling into the void."
    override val cost = 200
    override val duration = 5 * 20
    override var isActive = false

    private val listener = object : Listener {
        @EventHandler
        fun onPlayerKill(event: PlayerDeathEvent) {
            // Example listener
        }
    }

    override fun activate() {
        isActive = true
        Bukkit.getPluginManager().registerEvents(listener, ElytraPVP.instance!!)
    }

    override fun deactivate() {
        isActive = false
        PlayerDeathEvent.getHandlerList().unregister(listener)
    }
}