package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object MiscUtils {

    fun shopPurchaseItem(player: Player, price: Int, material: Material, amount: Int) {
        val gold = CacheConfig.getplrVal(player, "gold") as Int

        if (gold < price) {
            player.sendMessage(parse("&c&lEcoWings &8| &fNot enough gold! You need &6${price}g"))
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        val itemStack = ItemStack(material, amount)
        if (!hasInventorySpace(player, itemStack)) {
            player.sendMessage(parse("&c&lEcoWings &8| &fNot enough space in your inventory!"))
            player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        CacheConfig.setplrVal(player, "gold", gold - price)
        player.inventory.addItem(itemStack)
        player.sendMessage(parse("&a&lEcoWings &8| &fYou purchased &6${material.name} &ffor &6${price}g!"))
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
    }

    private fun hasInventorySpace(player: Player, itemStack: ItemStack): Boolean {
        val inventory = player.inventory
        var remainingAmount = itemStack.amount

        for (item in inventory.storageContents) {
            if (item == null || item.type == Material.AIR) {
                return true
            }
            if (item.isSimilar(itemStack)) {
                val spaceInStack = item.maxStackSize - item.amount
                if (spaceInStack >= remainingAmount) {
                    return true
                }
                remainingAmount -= spaceInStack
            }
        }
        return false
    }
}