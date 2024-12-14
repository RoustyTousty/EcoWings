package me.roustytousty.elytrapvp.utility

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object MiscUtils {



    /*
        Checks if player has inventory space for a given ItemStack
     */
    fun hasInventorySpaceForItemStack(player: Player, itemStack: ItemStack): Boolean {
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