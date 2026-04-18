package me.roustytousty.elytrapvp.gui.cosmetics

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.cosmetic.*
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class CosmeticSelectMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val title = e.view.title
        val type = when (title) {
            "Select Pattern" -> CosmeticType.PATTERN
            "Select Material" -> CosmeticType.MATERIAL
            "Select Dye" -> CosmeticType.COLOR
            else -> return
        }

        e.isCancelled = true
        val clickedItem = e.currentItem ?: return
        if (clickedItem.type.isAir) return

        val p = e.whoClicked as Player
        val service = Services.cosmeticService

        when (e.rawSlot) {
            4 -> {
                when (type) {
                    CosmeticType.PATTERN -> service.unequipPattern(p)
                    CosmeticType.MATERIAL -> service.unequipMaterial(p)
                    CosmeticType.COLOR -> service.unequipColor(p)
                }
                Services.kitService.syncKit(p)
                SoundUtils.playSuccess(p)
                CosmeticMenu.openInventory(p)
                return
            }
            45 -> {
                CosmeticMenu.openInventory(p)
                SoundUtils.playGuiClick(p)
                return
            }
        }

        if (!COSMETIC_SLOTS.contains(e.rawSlot)) return

        val items: List<ICosmetic> = when (type) {
            CosmeticType.PATTERN -> CosmeticPattern.getSortedPatterns()
            CosmeticType.MATERIAL -> CosmeticMaterial.getSortedMaterials()
            CosmeticType.COLOR -> CosmeticColor.getSortedColors()
        }

        val index = COSMETIC_SLOTS.indexOf(e.rawSlot)
        if (index >= items.size) return
        val selected = items[index]

        val isUnlocked = when (type) {
            CosmeticType.PATTERN -> service.hasUnlockedPattern(p, selected as CosmeticPattern)
            CosmeticType.MATERIAL -> service.hasUnlockedMaterial(p, selected as CosmeticMaterial)
            CosmeticType.COLOR -> service.hasUnlockedColor(p, selected as CosmeticColor)
        }

        if (isUnlocked) {
            when (type) {
                CosmeticType.PATTERN -> service.equipPattern(p, selected as CosmeticPattern)
                CosmeticType.MATERIAL -> service.equipMaterial(p, selected as CosmeticMaterial)
                CosmeticType.COLOR -> service.equipColor(p, selected as CosmeticColor)
            }
            Services.kitService.syncKit(p)
            SoundUtils.playSuccess(p)
            CosmeticMenu.openInventory(p)
        } else {
            if (selected.requiresEco && !p.hasPermission("elytrapvp.rank.eco")) {
                MessageUtils.sendError(p, "&fYou need the &6Eco &crank to unlock this!")
                SoundUtils.playFailure(p)
                return
            }
            ConfirmCosmeticPurchaseMenu.openInventory(p, selected, type)
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        val title = e.view.title
        if (title == "Select Pattern" || title == "Select Material" || title == "Select Dye") e.isCancelled = true
    }

    companion object {
        private val COSMETIC_SLOTS = listOf(
            11, 12, 13, 14, 15,
            20, 21, 22, 23, 24,
            29, 30, 31, 32, 33,
            38, 39, 40, 41, 42
        )

        fun openInventory(player: Player, type: CosmeticType) {
            val title = when (type) {
                CosmeticType.PATTERN -> "Select Pattern"
                CosmeticType.MATERIAL -> "Select Material"
                CosmeticType.COLOR -> "Select Dye"
            }
            val inventory = Bukkit.createInventory(null, 54, title)
            initItems(inventory, player, type)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(inventory: Inventory, player: Player, type: CosmeticType) {
            val borderSlots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35, 36, 44, 53)
            for (slot in borderSlots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            inventory.setItem(45, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cBack"))
            inventory.setItem(4, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cRemove Selection", "&7Click to clear this slot."))

            val items: List<ICosmetic> = when (type) {
                CosmeticType.PATTERN -> CosmeticPattern.getSortedPatterns()
                CosmeticType.MATERIAL -> CosmeticMaterial.getSortedMaterials()
                CosmeticType.COLOR -> CosmeticColor.getSortedColors()
            }

            val data = Services.playerService.getOrCreatePlayerData(player)
            val service = Services.cosmeticService

            val activeId = when (type) {
                CosmeticType.PATTERN -> data.activeTrimPattern
                CosmeticType.MATERIAL -> data.activeTrimMaterial
                CosmeticType.COLOR -> data.activeArmorColor
            }

            for ((index, item) in items.withIndex()) {
                if (index >= COSMETIC_SLOTS.size) break

                val isUnlocked = when (type) {
                    CosmeticType.PATTERN -> service.hasUnlockedPattern(player, item as CosmeticPattern)
                    CosmeticType.MATERIAL -> service.hasUnlockedMaterial(player, item as CosmeticMaterial)
                    CosmeticType.COLOR -> service.hasUnlockedColor(player, item as CosmeticColor)
                }

                val isEquipped = activeId == item.id

                val material = when {
                    isUnlocked -> item.icon
                    item.requiresEco -> Material.ORANGE_STAINED_GLASS_PANE
                    else -> Material.GRAY_STAINED_GLASS_PANE
                }

                val lore = mutableListOf("")

                if (isUnlocked) {
                    lore.add(if (isEquipped) "&aEquipped!" else "&7Click to equip!")
                } else {
                    if (item.goldCost > 0) lore.add("&fCost: &6${item.goldCost} Gold")
                    if (item.shardCost > 0) lore.add("&fCost: &b${item.shardCost} Shards")
                    if (item.requiresEco) lore.add("&cRequires Eco Rank!")
                    lore.add("")
                    lore.add("&7Click to purchase!")
                }

                inventory.setItem(COSMETIC_SLOTS[index], itemBuilder(material, 1, isEquipped, "&e${item.displayName}", *lore.toTypedArray()))
            }
        }
    }
}