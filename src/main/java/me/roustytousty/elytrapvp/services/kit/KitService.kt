package me.roustytousty.elytrapvp.services.kit

import org.bukkit.entity.Player

class KitService {

    private val playerKits = mutableMapOf<Player, Kit>()

    fun giveKit(player: Player) {
        val kit = getKit(player)
        kit.equip()
    }

    fun removeFromKitList(player: Player) {
        playerKits.remove(player)
    }

    private fun getKit(player: Player): Kit {
        return playerKits.computeIfAbsent(player) { Kit(player) }
    }
}