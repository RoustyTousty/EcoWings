package me.roustytousty.elytrapvp.utility.bounty

import me.roustytousty.elytrapvp.ElytraPVP
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class BountyManager {

    private val activeBounties = mutableMapOf<Player, Bounty>()

    fun applyBounty(player: Player, bountyAmount: Int) {
        val bounty = activeBounties[player]?.apply {
            update(bountyAmount)
        } ?: createBounty(player, bountyAmount)

        bountyHologramUpdator(player, bounty)
    }

    fun removeBounty(player: Player) {
        activeBounties.remove(player)
    }

    private fun createBounty(player: Player, bountyAmount: Int) : Bounty {
        val bounty = Bounty(player, bountyAmount)
        activeBounties[player] = bounty
        return bounty
    }

    private fun bountyHologramUpdator(player: Player, bounty: Bounty) {
        object : BukkitRunnable() {
            override fun run() {

                if (!isBountyActive(player)) {
                    cancel()
                    return
                }

                bounty.updateHologramPosition()
            }
        }.runTaskTimer(ElytraPVP.instance!!, 0, 1)
    }

    private fun isBountyActive(player: Player): Boolean {
        return activeBounties.containsKey(player)
    }
}