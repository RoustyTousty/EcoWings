package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

object ItemUtils {



    /*
        Creates a generic ItemStack
     */
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

        meta.addItemFlags(
            ItemFlag.HIDE_ATTRIBUTES,
            ItemFlag.HIDE_ENCHANTS,
            ItemFlag.HIDE_UNBREAKABLE,
            ItemFlag.HIDE_ADDITIONAL_TOOLTIP,
            ItemFlag.HIDE_ARMOR_TRIM,
            ItemFlag.HIDE_STORED_ENCHANTS
        )

        if (lore.isNotEmpty()) {
            val l: MutableList<String> = ArrayList()
            for (s in lore) {
                l.add(FormatUtils.parse(s))
            }
            meta.lore = l
        }

        if (glow) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        return item
    }



    /*
        Creates a player head ItemStack
     */
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
            meta.addEnchant(Enchantment.UNBREAKING, 1, false)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        return item
    }



    fun itemBuilder(
        base: ItemStack,
        name: String? = null,
        vararg lore: String?
    ): ItemStack {
        val item = base.clone()
        val meta = item.itemMeta ?: return item

        if (name != null) {
            meta.setDisplayName(FormatUtils.parse(name))
        }

        if (lore.isNotEmpty()) {
            val l = lore.mapNotNull { FormatUtils.parse(it) }
            meta.lore = l
        }

        item.itemMeta = meta
        return item
    }



    /*
        Creates an ItemStack based on a specific UpgradeConfig item
     */
    fun kitItemBuilder(
        config: ConfigurationSection,
        type: UpgradeType,
        plugin: JavaPlugin
    ): ItemStack {
        val material = Material.getMaterial(config.getString("material", "AIR")!!) ?: Material.AIR
        val itemStack = ItemStack(material)
        val meta: ItemMeta = itemStack.itemMeta ?: return itemStack

        meta.isUnbreakable = true
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE)

        meta.setDisplayName(
            FormatUtils.parse(config.getString("name", ""))
        )

        val key = NamespacedKey(plugin, "kit-item")
        meta.persistentDataContainer.set(
            key,
            PersistentDataType.STRING,
            type.name
        )

        config.getConfigurationSection("enchants")?.let { enchants ->
            for (k in enchants.getKeys(false)) {
                val enchant = Enchantment.getByName(k.uppercase()) ?: continue
                val level = enchants.getInt(k, 1)
                meta.addEnchant(enchant, level, true)
            }
        }

        itemStack.itemMeta = meta
        return itemStack
    }
}