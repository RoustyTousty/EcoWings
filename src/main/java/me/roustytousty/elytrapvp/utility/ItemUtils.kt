package me.roustytousty.elytrapvp.utility

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta

object ItemUtils {

    fun itemBuilder(
        material: Material,
        amount: Int,
        glow: Boolean,
        name: String?,
        vararg lore: String?
    ): ItemStack {
        val item = ItemStack(material, amount)
        val meta = item.itemMeta

        meta.setDisplayName(FormatUtils.parse(name))

        meta.attributeModifiers = item.type.getDefaultAttributeModifiers(EquipmentSlot.HAND)
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        if (lore.isNotEmpty()) {
            val l: MutableList<String> = ArrayList()
            for (s in lore) {
                l.add(FormatUtils.parse(s))
            }
            meta.lore = l
        }

        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        return item
    }

    fun itemBuilder(
        player: OfflinePlayer,
        amount: Int,
        glow: Boolean,
        name: String?,
        vararg lore: String?
    ): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD, amount)
        val meta = item.itemMeta as SkullMeta

        meta.owningPlayer = player

        meta.setDisplayName(FormatUtils.parse(name))

        if (lore.isNotEmpty()) {
            val l: MutableList<String> = ArrayList()
            for (s in lore) {
                l.add(FormatUtils.parse(s))
            }
            meta.lore = l
        }

        if (glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        return item
    }

    fun kitItemBuilder(
        config: ConfigurationSection
    ): ItemStack {
        val material = Material.getMaterial(config.getString("material", "AIR")!!) ?: Material.AIR
        val itemStack = ItemStack(material)
        val meta: ItemMeta = itemStack.itemMeta ?: return itemStack

        meta.isUnbreakable = true

        meta.attributeModifiers = itemStack.type.getDefaultAttributeModifiers(EquipmentSlot.HAND)
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE)

        meta.setDisplayName(config.getString("name", ""))
        config.getConfigurationSection("enchants")?.let { enchants ->
            for (key in enchants.getKeys(false)) {
                val enchant = Enchantment.getByName(key.uppercase()) ?: continue
                val level = enchants.getInt(key, 1)
                meta.addEnchant(enchant, level, true)
            }
        }
        itemStack.itemMeta = meta
        return itemStack
    }
}