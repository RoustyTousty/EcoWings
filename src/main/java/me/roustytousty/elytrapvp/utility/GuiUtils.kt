package me.roustytousty.elytrapvp.utility

import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object GuiUtils {

    fun createGuiItem(
        material: Material,
        ammount: Int,
        glow: Boolean,
        name: String?,
        vararg lore: String?
    ): ItemStack {
        val item = ItemStack(material, ammount)
        val meta = item.itemMeta

        // Set name
        meta.setDisplayName(StringUtils.parse(name))

        meta.attributeModifiers = item.type.getDefaultAttributeModifiers(EquipmentSlot.HAND)
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)

        // Set lore
        if (lore.size > 0) {
            val l: MutableList<String> = ArrayList()
            for (s in lore) {
                l.add(StringUtils.parse(s))
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

    fun createPlayerHead(player: OfflinePlayer, name: String?, vararg lore: String?): ItemStack {
        val item = ItemStack(Material.PLAYER_HEAD, 1)
        val meta = item.itemMeta as SkullMeta

        meta.owningPlayer = player

        // Set the name of the item
        meta.setDisplayName(StringUtils.parse(name))

        // Set the lore of the item
        if (lore.isNotEmpty()) {
            val l: MutableList<String> = ArrayList()
            for (s in lore) {
                l.add(StringUtils.parse(s))
            }
            meta.lore = l
        }

        item.itemMeta = meta
        return item
    }
}