package me.roustytousty.elytrapvp.services.shop

import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.MiscUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShopService(
    private val playerService: PlayerService
) {

    fun shopPurchaseItem(player: Player, price: Int, material: Material, amount: Int) {
        val playerData = playerService.getOrCreatePlayerData(player)

        if (playerData.gold < price) {
            MessageUtils.sendError(player, "&fNot enough gold! You need &6&l${price}g&f!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        val itemStack = ItemStack(material, amount)
        if (!MiscUtils.hasInventorySpaceForItemStack(player, itemStack)) {
            MessageUtils.sendError(player, "&fNot enough space in your inventory!")
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        playerData.gold -= price

        player.inventory.addItem(itemStack)
        MessageUtils.sendSuccess(player, "&fYou purchased &6&l${material.name} &ffor &6&l${price}g&f!")
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
    }
}