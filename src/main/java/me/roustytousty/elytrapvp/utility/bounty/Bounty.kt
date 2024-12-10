package me.roustytousty.elytrapvp.utility.bounty

import eu.decentsoftware.holograms.api.DHAPI
import eu.decentsoftware.holograms.api.holograms.Hologram
import org.bukkit.Location
import org.bukkit.entity.Player

class Bounty(private val player: Player, initialBountyAmount: Int) {

    var amount: Int = initialBountyAmount
        private set

    private val hologram: Hologram = getOrCreateHologram(player)

    init {
        update(initialBountyAmount)
    }

    fun update(newAmount: Int) {
        amount = newAmount
        DHAPI.setHologramLines(hologram, listOf(formatHologramText(newAmount)))
    }

    fun updateHologramPosition() {
        DHAPI.moveHologram(hologram, getHologramLocation(player))
    }

    private fun formatHologramText(amount: Int): String {
        return "&6&l${amount} GOLD"
    }

    private fun getHologramLocation(player: Player): Location {
        return player.location.add(0.0, 2.5, 0.0)
    }

    private fun getOrCreateHologram(player: Player): Hologram {
        val existingHologram = DHAPI.getHologram(player.name)
        return if (existingHologram != null) {
            existingHologram
        } else {
            DHAPI.createHologram(
                player.name,
                getHologramLocation(player),
                listOf(formatHologramText(0))
            )
//            ).apply { setHidePlayer(player) }
        }
    }
}