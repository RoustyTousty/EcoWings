package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

object KitUtils {

    fun givePlayerKit(player: Player) {
        giveArmorPiece(player, "helmet", "helmet")
        giveArmorPiece(player, "elytra", "chestplate")
        giveArmorPiece(player, "leggings", "leggings")
        giveArmorPiece(player, "boots", "boots")

        giveTool(player, "sword", 0)
        giveTool(player, "shears", 1)

        giveWhiteWool(player, 16)
    }

    private fun giveArmorPiece(player: Player, configKey: String, armorSlot: String) {
        val config = UpgradeConfig.getConfig()
        val level = CacheConfig.getplrVal(player, "${configKey}Level") as? Int ?: 0
        val itemStack = createKitItem(config.getConfigurationSection("upgrades.$configKey.$level")!!)
        when (armorSlot) {
            "helmet" -> player.inventory.helmet = itemStack
            "chestplate" -> player.inventory.chestplate = itemStack
            "leggings" -> player.inventory.leggings = itemStack
            "boots" -> player.inventory.boots = itemStack
        }
    }

    private fun giveTool(player: Player, configKey: String, defaultSlot: Int) {
        val config = UpgradeConfig.getConfig()
        val level = CacheConfig.getplrVal(player, "${configKey}Level") as? Int ?: 0
        val itemStack = createKitItem(config.getConfigurationSection("upgrades.$configKey.$level")!!)

        val existingSlot = player.inventory.contents.indexOfFirst { it != null && it.type == itemStack.type }

        if (existingSlot != -1) {
            player.inventory.setItem(existingSlot, itemStack)
        } else {
            player.inventory.setItem(defaultSlot, itemStack)
        }
    }

    private fun giveWhiteWool(player: Player, amount: Int) {
        val hasBlocks = player.inventory.contents.any { it != null && it.type.isBlock }

        if (!hasBlocks && player.inventory.firstEmpty() != -1) {
            val wool = ItemStack(Material.WHITE_WOOL, amount)
            player.inventory.addItem(wool)
        }
    }

    fun createKitItem(kitSection: ConfigurationSection): ItemStack {
        val materialName = kitSection.getString("material")
        val material = Material.getMaterial(materialName ?: throw IllegalArgumentException("Invalid material: $materialName"))!!
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item

        meta.setDisplayName(StringUtils.parse(kitSection.getString("name")!!))

        meta.isUnbreakable = true

        meta.attributeModifiers = item.type.getDefaultAttributeModifiers(EquipmentSlot.HAND)
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)

        kitSection.getConfigurationSection("enchants")?.getValues(false)?.let { enchants ->

            (enchants as Map<String, Int>).forEach { (enchantName, level) ->

                Enchantment.getByName(enchantName)?.let { enchantment ->
                    meta.addEnchant(enchantment, level, true)
                }
            }
        }

        item.itemMeta = meta

        return item
    }
}