package me.roustytousty.elytrapvp.services.kit

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import me.roustytousty.elytrapvp.utility.ItemUtils
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Kit(
    private val player: Player
) {

    fun equip() {
        equipArmor()
        equipItem("sword", 0)
        equipItem("shears", 1)
    }

    private fun equipArmor() {
        player.inventory.helmet = getItem("helmet")
        player.inventory.chestplate = getItem("elytra")
        player.inventory.leggings = getItem("leggings")
        player.inventory.boots = getItem("boots")
    }

    private fun equipItem(item: String, slot: Int) {
        val itemStack = getItem(item) ?: return
        val existingSlot = player.inventory.contents.indexOfFirst { it?.type == itemStack.type }
        if (existingSlot != -1) {
            player.inventory.setItem(existingSlot, itemStack)
        } else {
            player.inventory.setItem(slot, itemStack)
        }
    }

    private fun getItem(item: String): ItemStack? {
        val level = CacheConfig.getplrVal(player, "${item}Level") as? Int ?: 0
        val configSection = UpgradeConfig.getConfig().getConfigurationSection("upgrades.$item.$level") ?: return null
        return ItemUtils.kitItemBuilder(configSection)
    }
}