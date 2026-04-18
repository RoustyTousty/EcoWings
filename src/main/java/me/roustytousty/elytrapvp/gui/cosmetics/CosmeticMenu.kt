package me.roustytousty.elytrapvp.gui.cosmetics

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.cosmetic.CosmeticColor
import me.roustytousty.elytrapvp.services.cosmetic.CosmeticMaterial
import me.roustytousty.elytrapvp.services.cosmetic.CosmeticPattern
import me.roustytousty.elytrapvp.services.cosmetic.CosmeticType
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class CosmeticMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.view.title != "Armor Cosmetics") return
        e.isCancelled = true

        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            11 -> CosmeticSelectMenu.openInventory(p, CosmeticType.PATTERN)
            13 -> CosmeticSelectMenu.openInventory(p, CosmeticType.MATERIAL)
            15 -> CosmeticSelectMenu.openInventory(p, CosmeticType.COLOR)
            18 -> {
                p.closeInventory()
                SoundUtils.playGuiClick(p)
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Armor Cosmetics") e.isCancelled = true
    }

    companion object {
        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 27, "Armor Cosmetics")
            initItems(inventory, player)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory, player: Player) {
            val slots = intArrayOf(0, 8, 9, 17, 26)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            val data = Services.playerService.getOrCreatePlayerData(player)
            val pattern = CosmeticPattern.fromId(data.activeTrimPattern)
            val mat = CosmeticMaterial.fromId(data.activeTrimMaterial)
            val color = CosmeticColor.fromId(data.activeArmorColor)

            inventory.setItem(11, itemBuilder(
                pattern?.icon ?: Material.LIME_STAINED_GLASS_PANE, 1, pattern != null,
                "&ePatterns", "", "&fCurrent: &6${pattern?.displayName ?: "&6None"}", "", "&7Click to select a pattern!"
            ))

            inventory.setItem(13, itemBuilder(
                mat?.icon ?: Material.LIME_STAINED_GLASS_PANE, 1, mat != null,
                "&eMaterials", "", "&fCurrent: &6${mat?.displayName ?: "&6None"}", "", "&7Click to select a material!"
            ))

            inventory.setItem(15, itemBuilder(
                color?.icon ?: Material.LIME_STAINED_GLASS_PANE, 1, color != null,
                "&eDyes", "", "&fCurrent: &6${color?.displayName ?: "&6None"}", "", "&7Click to select a color!"
            ))

            inventory.setItem(18, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cClose"))
        }
    }
}