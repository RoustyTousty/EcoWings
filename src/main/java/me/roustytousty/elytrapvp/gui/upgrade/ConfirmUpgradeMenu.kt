package me.roustytousty.elytrapvp.gui.upgrade

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import me.roustytousty.elytrapvp.services.kit.KitService
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.FormatUtils
import me.roustytousty.elytrapvp.utility.ItemUtils
import me.roustytousty.elytrapvp.utility.MessageUtils
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

    private val kitService = KitService()

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
            // TODO: Move this to players service
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

                    kitService.giveKit(p)

                    MessageUtils.sendSuccess(p, "&fUpgrade successful! &6&l${item} &fis now level &6&l${nextItemLevel}&f!")
                    p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
                    p.closeInventory()
                } else {
                    MessageUtils.sendError(p, "&fNot enough gold! You need &6&l${upgradeCost}g&f!")
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
                val itemStack = ItemUtils.kitItemBuilder(itemConfigSection)
                inv!!.setItem(4, itemStack)

                val upgradeCost = itemConfigSection.getInt("cost")
                inv!!.setItem(0, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cCancel"))
                inv!!.setItem(8, itemBuilder(Material.LIME_STAINED_GLASS_PANE, 1, false, "&aConfirm", "", "&fPrice: &6${upgradeCost}g", "", "&7Click to confirm!"))
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