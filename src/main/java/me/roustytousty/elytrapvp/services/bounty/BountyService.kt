package me.roustytousty.elytrapvp.services.bounty;

import me.roustytousty.elytrapvp.ElytraPVP
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class BountyService {

    private val activeBounties = mutableMapOf<Player, Bounty>()

    fun applyBounty(player: Player, bountyAmount: Int) {
        val bounty = activeBounties[player]?.apply {
            update(bountyAmount)
        } ?: createBounty(player, bountyAmount)

        bountyHologramUpdateTask(player, bounty)
    }

    fun removeBounty(player: Player) {
        activeBounties.remove(player)
    }

    fun getBounty(player: Player): Bounty? {
        return activeBounties[player]
    }

    private fun createBounty(player: Player, bountyAmount: Int) : Bounty {
        val bounty = Bounty(player, bountyAmount)
        activeBounties[player] = bounty
        return bounty
    }

    private fun bountyHologramUpdateTask(player: Player, bounty: Bounty) {
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
