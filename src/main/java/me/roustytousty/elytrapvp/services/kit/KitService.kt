package me.roustytousty.elytrapvp.services.kit

import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.services.cosmetic.CosmeticService
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.services.shop.ShopService
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
    private val shopService: ShopService,
    private val cosmeticService: CosmeticService,
    private val plugin: JavaPlugin
) {

    private val key = NamespacedKey(plugin, "kit-item")

    fun syncKit(player: Player) {
        val data = playerService.getOrCreatePlayerData(player)

        purgeInvalidItems(player)

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

        val helmet = upgradeService.getItem(data, UpgradeType.HELMET)
        val chestplate = upgradeService.getItem(data, UpgradeType.ELYTRA)
        val leggings = upgradeService.getItem(data, UpgradeType.LEGGINGS)
        val boots = upgradeService.getItem(data, UpgradeType.BOOTS)

        helmet?.let {
            cosmeticService.applyTrimToItem(player, it)
            cosmeticService.applyColorToItem(player, it)
        }
        leggings?.let {
            cosmeticService.applyTrimToItem(player, it)
            cosmeticService.applyColorToItem(player, it)
        }
        boots?.let {
            cosmeticService.applyTrimToItem(player, it)
            cosmeticService.applyColorToItem(player, it)
        }

        inv.helmet = helmet
        inv.chestplate = chestplate
        inv.leggings = leggings
        inv.boots = boots
    }


    private fun applyItem(player: Player, data: PlayerData, type: UpgradeType) {
        val item = upgradeService.getItem(data, type) ?: return
        val inv = player.inventory

        val existingSlot = inv.contents.indexOfFirst { isKitItem(it, type) }

        if (existingSlot != -1) {
            inv.setItem(existingSlot, item)
            return
        }

        val empty = inv.firstEmpty()
        if (empty != -1) {
            inv.setItem(empty, item)
            return
        }

        for ((index, stack) in inv.contents.withIndex()) {
            if (stack == null) continue

            if (!isKitItem(stack)) {
                inv.setItem(index, item)
                return
            }
        }
    }


    private fun ensureBlocks(player: Player) {
        val inv = player.inventory
        val woolMaterial = Material.WHITE_WOOL

        val woolSlot = inv.contents.indexOfFirst { it?.type == woolMaterial }

        if (woolSlot == -1) {
            val empty = inv.firstEmpty()
            if (empty != -1) {
                inv.setItem(empty, shopService.getFormattedItem(woolMaterial, 32))
            }
            return
        }

        val stack = inv.getItem(woolSlot) ?: return

        val shopVersion = shopService.getFormattedItem(woolMaterial, 1)
        if (stack.itemMeta?.displayName != shopVersion.itemMeta?.displayName) {
            stack.itemMeta = shopVersion.itemMeta
        }

        val current = stack.amount
        if (current >= 32) return

        val needed = 32 - current
        stack.amount = current + needed
        inv.setItem(woolSlot, stack)
    }

    private fun purgeInvalidItems(player: Player) {
        val inv = player.inventory

        val kitMaterials = setOf(
            Material.WOODEN_SWORD, Material.WOODEN_PICKAXE, Material.WOODEN_AXE, Material.SHEARS,
            Material.LEATHER_HELMET, Material.ELYTRA, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS
        )

        inv.contents.forEachIndexed { index, item ->
            if (item == null) return@forEachIndexed

            val isKitMaterial = item.type in kitMaterials

            if (isKitMaterial && !isKitItem(item)) {
                inv.setItem(index, null)
            }
        }
    }
}