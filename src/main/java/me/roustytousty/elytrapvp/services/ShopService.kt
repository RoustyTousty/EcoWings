package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.MiscUtils
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ShopService {

    fun shopPurchaseItem(player: Player, price: Int, material: Material, amount: Int) {
        val gold = CacheConfig.getplrVal(player, "gold") as Int

        if (gold < price) {
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

        CacheConfig.setplrVal(player, "gold", gold - price)
        player.inventory.addItem(itemStack)
        MessageUtils.sendSuccess(player, "&fYou purchased &6&l${material.name} &ffor &6&l${price}g&f!")
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
    }
}