package me.roustytousty.elytrapvp.services.event.events

import com.destroystokyo.paper.event.player.PlayerJumpEvent
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
    override var contributions = 0
    override val duration = 5 * 60
    override var isActive = false

    private val listener = object : Listener {
        @EventHandler
        fun onPlayerKill(event: PlayerDeathEvent) {
            // Example listener
        }

        @EventHandler
        fun onPlayerJump(event: PlayerJumpEvent) {

        }
    }

    override fun activate() {
        isActive = true
        Bukkit.getPluginManager().registerEvents(listener, ElytraPVP.instance!!)
        createFloor()
    }

    override fun deactivate() {
        isActive = false
        PlayerDeathEvent.getHandlerList().unregister(listener)
        removeFloor()
    }


    private fun createFloor() {

    }

    private fun removeFloor() {

    }
}