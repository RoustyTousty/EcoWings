package me.roustytousty.elytrapvp.gui.upgrade

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.FormatUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue

class ConfirmUpgradeMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        if (e.rawSlot == 0) {
            UpgradeMenu.openInventory(p)
            p.playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        } else if (e.rawSlot == 8) {
            val item = getItemFromInventory(p)
            val itemLevel = CacheConfig.getplrVal(p, "${item}Level") as? Int ?: 0
            val nextItemLevel = itemLevel + 1
            val itemConfigSection = UpgradeConfig.getConfig().getConfigurationSection("upgrades.$item.$nextItemLevel")

            if (itemConfigSection != null) {
                val upgradeCost = itemConfigSection.getInt("cost")
                val currentGold = CacheConfig.getplrVal(p, "gold") as? Int ?: 0

                if (currentGold >= upgradeCost) {
                    CacheConfig.setplrVal(p, "gold", currentGold - upgradeCost)

                    CacheConfig.setplrVal(p, "${item}Level", nextItemLevel)

                    ItemUtils.givePlayerKit(p)

                    p.sendMessage(FormatUtils.parse("&a&lEcoWings &8| &fUpgrade successful! &6${item} &fis now level &6$nextItemLevel"))
                    p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                    p.closeInventory()
                } else {
                    p.sendMessage(FormatUtils.parse("&c&lEcoWings &8| &fNot enough gold! You need &6${upgradeCost}g"))
                    p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
                }
            }
        }
    }

    @EventHandler
    private fun onInventoryClick(e: InventoryDragEvent) {
        if (e.inventory == inv) {
            e.isCancelled = true
        }
    }

    companion object {

        var inv: Inventory? = null
        fun openInventory(player: Player, item: String) {
            inv = Bukkit.createInventory(null, 9, "Confirm upgrade")
            initItems(player, item)
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
            player.setMetadata("upgradeItem", FixedMetadataValue(ElytraPVP.instance!!, item))
        }

        private fun initItems(player: Player, item: String) {
            val slots = intArrayOf(1, 2, 3, 5, 6, 7)
            for (slot in slots) {
                inv!!.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            val itemLevel = CacheConfig.getplrVal(player, "${item}Level") as? Int ?: 0
            val nextItemLevel = itemLevel + 1
            val itemConfigSection = UpgradeConfig.getConfig().getConfigurationSection("upgrades.$item.$nextItemLevel")

            if (itemConfigSection != null) {
                val itemStack = ItemUtils.createKitItem(itemConfigSection)
                inv!!.setItem(4, itemStack)

                val upgradeCost = itemConfigSection.getInt("cost")
                inv!!.setItem(0, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cCancel"))
                inv!!.setItem(8, itemBuilder(Material.LIME_STAINED_GLASS_PANE, 1, false, "&aConfirm", "", "&fCost: &6${upgradeCost}g", "", "&7Click to confirm!"))
            }
        }

        private fun getItemFromInventory(player: Player): String {
            val metadata: List<MetadataValue> = player.getMetadata("upgradeItem")
            return if (metadata.isNotEmpty()) {
                metadata[0].asString()
            } else {
                "unknown"
            }
        }
    }
}