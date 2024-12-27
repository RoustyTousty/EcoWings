package me.roustytousty.elytrapvp.services.kit

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import me.roustytousty.elytrapvp.utility.ItemUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Kit(
    private val player: Player
) {

    fun equip() {
        equipArmor()
        equipItem("sword", 0)
        equipItem("shears", 1)
        checkAndGiveWool()
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

    private fun checkAndGiveWool() {
        val buildingBlocks = setOf(Material.WHITE_WOOL, Material.YELLOW_WOOL, Material.ORANGE_WOOL, Material.WHITE_CONCRETE, Material.YELLOW_CONCRETE)

        val hasBuildingBlocks = player.inventory.contents.any { it?.type in buildingBlocks }

        if (!hasBuildingBlocks) {
            val whiteWool = ItemStack(Material.WHITE_WOOL, 16)
            val emptySlot = player.inventory.firstEmpty()
            if (emptySlot != -1) {
                player.inventory.setItem(emptySlot, whiteWool)
            } else {
                player.world.dropItem(player.location, whiteWool)
            }
        }
    }
}