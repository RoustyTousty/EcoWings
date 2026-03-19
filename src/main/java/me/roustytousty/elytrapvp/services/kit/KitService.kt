package me.roustytousty.elytrapvp.services.kit

import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.services.upgrade.UpgradeService
import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class KitService(
    private val playerService: PlayerService,
    private val upgradeService: UpgradeService,
    private val plugin: JavaPlugin
) {

    private val key = NamespacedKey(plugin, "kit-item")

    fun syncKit(player: Player) {
        val data = playerService.getOrCreatePlayerData(player)

        applyArmor(player, data)
        applyItem(player, data, UpgradeType.SWORD)
        applyItem(player, data, UpgradeType.SHEARS)
        applyItem(player, data, UpgradeType.PICKAXE)
        applyItem(player, data, UpgradeType.AXE)

        ensureBlocks(player)
    }

    fun isKitItem(item: ItemStack?): Boolean {
        if (item == null) return false
        val meta = item.itemMeta ?: return false

        return meta.persistentDataContainer.has(key, PersistentDataType.STRING)
    }

    private fun isKitItem(item: ItemStack?, type: UpgradeType): Boolean {
        if (item == null) return false
        val meta = item.itemMeta ?: return false

        val value = meta.persistentDataContainer.get(key, PersistentDataType.STRING)
        return value == type.name
    }

    private fun applyArmor(player: Player, data: PlayerData) {
        val inv = player.inventory

        inv.helmet = upgradeService.getItem(data, UpgradeType.HELMET)
        inv.chestplate = upgradeService.getItem(data, UpgradeType.ELYTRA)
        inv.leggings = upgradeService.getItem(data, UpgradeType.LEGGINGS)
        inv.boots = upgradeService.getItem(data, UpgradeType.BOOTS)
    }


    private fun applyItem(player: Player, data: PlayerData, type: UpgradeType) {
        val item = upgradeService.getItem(data, type) ?: return
        val inv = player.inventory

        val existingSlot = inv.contents.indexOfFirst { isKitItem(it, type) }

        if (existingSlot != -1) {
            inv.setItem(existingSlot, item)
        } else {
            val empty = inv.firstEmpty()
            if (empty != -1) {
                inv.setItem(empty, item)
            }
        }
    }


    private fun ensureBlocks(player: Player) {
        val buildingBlocks = setOf(
            Material.WHITE_WOOL,
            Material.YELLOW_WOOL,
            Material.ORANGE_WOOL,
            Material.WHITE_CONCRETE,
            Material.YELLOW_CONCRETE
        )

        val hasBlocks = player.inventory.contents.any { it?.type in buildingBlocks }

        if (!hasBlocks) {
            val stack = ItemStack(Material.WHITE_WOOL, 16)
            val slot = player.inventory.firstEmpty()

            if (slot != -1) {
                player.inventory.setItem(slot, stack)
            } else {
                player.world.dropItem(player.location, stack)
            }
        }
    }
}