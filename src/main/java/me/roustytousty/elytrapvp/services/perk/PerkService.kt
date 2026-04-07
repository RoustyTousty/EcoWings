package me.roustytousty.elytrapvp.services.perk

import me.roustytousty.elytrapvp.services.player.PlayerService
import org.bukkit.entity.Player

class PerkService(
    private val playerService: PlayerService
) {
    val slot2Cost = 15
    val slot3Cost = 35

    fun hasPerkEquipped(player: Player, perk: PerkType): Boolean {
        val playerData = playerService.getOrCreatePlayerData(player)
        return playerData.equippedPerks.contains(perk.id)
    }

    fun hasUnlockedPerk(player: Player, perk: PerkType): Boolean {
        val playerData = playerService.getOrCreatePlayerData(player)
        return playerData.unlockedPerks.contains(perk.id)
    }

    fun tryPlayerPurchasePerk(player: Player, perk: PerkType): Boolean {
        val playerData = playerService.getOrCreatePlayerData(player)

        if (hasUnlockedPerk(player, perk)) return false

        if (perk.requiresEco && !player.hasPermission("elytrapvp.rank.eco")) return false
        if (playerData.gold < perk.goldCost) return false
        if (playerData.shards < perk.shardCost) return false

        playerData.gold -= perk.goldCost
        playerData.shards -= perk.shardCost
        playerData.unlockedPerks.add(perk.id)
        return true
    }

    fun equipPerk(player: Player, slotIndex: Int, perk: PerkType): Boolean {
        val playerData = playerService.getOrCreatePlayerData(player)

        if (slotIndex !in 0..2) return false
        if (slotIndex >= playerData.unlockedPerkSlots) return false
        if (!hasUnlockedPerk(player, perk)) return false

        if (playerData.equippedPerks.contains(perk.id)) return false

        playerData.equippedPerks[slotIndex] = perk.id
        return true
    }

    fun unequipPerk(player: Player, slotIndex: Int) {
        val playerData = playerService.getOrCreatePlayerData(player)
        if (slotIndex in 0..2) {
            playerData.equippedPerks[slotIndex] = ""
        }
    }

    fun tryUnlockSlot(player: Player, targetSlotIndex: Int): Boolean {
        val playerData = playerService.getOrCreatePlayerData(player)
        if (playerData.unlockedPerkSlots > targetSlotIndex) return false

        when (targetSlotIndex) {
            1 -> {
                if (playerData.shards < slot2Cost) return false
                playerData.shards -= slot2Cost
                playerData.unlockedPerkSlots = 2
                return true
            }
            2 -> {
                if (!player.hasPermission("elytrapvp.rank.eco")) return false
                if (playerData.shards < slot3Cost) return false
                playerData.shards -= slot3Cost
                playerData.unlockedPerkSlots = 3
                return true
            }
        }
        return false
    }
}