package me.roustytousty.elytrapvp.services.perk

import me.roustytousty.elytrapvp.services.player.PlayerService
import org.bukkit.entity.Player

class PerkService(
    private val playerService: PlayerService
) {

    fun hasPerkEquipped(player: Player, perk: PerkType): Boolean {
        val data = playerService.getOrCreatePlayerData(player)
        return data.equippedPerks.contains(perk.id)
    }

    fun hasUnlockedPerk(player: Player, perk: PerkType): Boolean {
        val data = playerService.getOrCreatePlayerData(player)
        return data.unlockedPerks.contains(perk.id)
    }

    fun buyPerk(player: Player, perk: PerkType): Boolean {
        val data = playerService.getOrCreatePlayerData(player)

        if (hasUnlockedPerk(player, perk)) return false
        if (data.gold < perk.cost) return false

        data.gold -= perk.cost
        data.unlockedPerks.add(perk.id)
        return true
    }

    fun equipPerk(player: Player, slotIndex: Int, perk: PerkType): Boolean {
        val data = playerService.getOrCreatePlayerData(player)

        if (slotIndex !in 0..2) return false
        if (slotIndex >= data.unlockedPerkSlots) return false
        if (!hasUnlockedPerk(player, perk)) return false

        if (data.equippedPerks.contains(perk.id)) return false

        data.equippedPerks[slotIndex] = perk.id
        return true
    }

    fun unequipPerk(player: Player, slotIndex: Int) {
        val data = playerService.getOrCreatePlayerData(player)
        if (slotIndex in 0..2) {
            data.equippedPerks[slotIndex] = ""
        }
    }

    fun unlockNextSlot(player: Player, cost: Int): Boolean {
        val data = playerService.getOrCreatePlayerData(player)
        if (data.unlockedPerkSlots >= 3) return false
        if (data.gold < cost) return false

        data.gold -= cost
        data.unlockedPerkSlots += 1
        return true
    }
}