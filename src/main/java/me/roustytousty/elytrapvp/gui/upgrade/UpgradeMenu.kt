package me.roustytousty.elytrapvp.gui.upgrade

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.data.UpgradeConfig
import me.roustytousty.elytrapvp.utility.GuiUtils.createGuiItem
import me.roustytousty.elytrapvp.utility.FormatUtils.formatNumber
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class UpgradeMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        val itemMeta = clickedItem.itemMeta ?: return
        val lore = itemMeta.lore ?: return

        if (lore.any { it.contains("MAXED", ignoreCase = true) }) {
            p.sendMessage(parse("&c&lEcoWings &8| &fThis item is already &c&lMAXED &fand cannot be upgraded further."))
            p.playSound(p, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
            return
        }

        if (e.rawSlot == 27) {
            p.closeInventory()
            p.playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        } else if (e.rawSlot == 11) {
            ConfirmUpgradeMenu.openInventory(p, "helmet")
        } else if (e.rawSlot == 12) {
            ConfirmUpgradeMenu.openInventory(p, "elytra")
        } else if (e.rawSlot == 13) {
            ConfirmUpgradeMenu.openInventory(p, "leggings")
        } else if (e.rawSlot == 14) {
            ConfirmUpgradeMenu.openInventory(p, "boots")
        } else if (e.rawSlot == 15) {
            ConfirmUpgradeMenu.openInventory(p, "sword")
        } else if (e.rawSlot == 22) {
            ConfirmUpgradeMenu.openInventory(p, "shears")
        } else if (e.rawSlot == 27) {
            p.closeInventory()
            p.playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
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
        fun openInventory(player: Player) {
            inv = Bukkit.createInventory(null, 36, "Upgrade")
            initItems(player)
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(player: Player) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35)
            for (slot in slots) {
                inv!!.setItem(slot, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            val nextHelmetLevel = ((CacheConfig.getplrVal(player, "helmetLevel") as? Int) ?: 0) + 1
            val nextElytraLevel = ((CacheConfig.getplrVal(player, "elytraLevel") as? Int) ?: 0) + 1
            val nextLeggingsLevel = ((CacheConfig.getplrVal(player, "leggingsLevel") as? Int) ?: 0) + 1
            val nextBootsLevel = ((CacheConfig.getplrVal(player, "bootsLevel") as? Int) ?: 0) + 1
            val nextSwordLevel = ((CacheConfig.getplrVal(player, "swordLevel") as? Int) ?: 0) + 1
            val nextShearsLevel = ((CacheConfig.getplrVal(player, "shearsLevel") as? Int) ?: 0) + 1

            val helmetCost = UpgradeConfig.getConfig().getInt("upgrades.helmet.$nextHelmetLevel.cost", -1)
            val elytraCost = UpgradeConfig.getConfig().getInt("upgrades.elytra.$nextElytraLevel.cost", -1)
            val leggingsCost = UpgradeConfig.getConfig().getInt("upgrades.leggings.$nextLeggingsLevel.cost", -1)
            val bootsCost = UpgradeConfig.getConfig().getInt("upgrades.boots.$nextBootsLevel.cost", -1)
            val swordCost = UpgradeConfig.getConfig().getInt("upgrades.sword.$nextSwordLevel.cost", -1)
            val shearsCost = UpgradeConfig.getConfig().getInt("upgrades.shears.$nextShearsLevel.cost", -1)

            // Helmet
            if (helmetCost == -1) {
                inv!!.setItem(11,
                    createGuiItem(
                        Material.LEATHER_HELMET,
                        1,
                        false,
                        "&eHelmet",
                        "&8Upgradable",
                        "",
                        "&c&lMAXED"
                    )
                )
            } else {
                inv!!.setItem(11,
                    createGuiItem(
                        Material.LEATHER_HELMET,
                        1,
                        false,
                        "&eHelmet",
                        "&8Upgradable",
                        "",
                        "&fCost: &6${formatNumber(helmetCost)}g",
                        "",
                        "&7Click to upgrade!"
                    )
                )
            }

            // Elytra
            if (elytraCost == -1) {
                inv!!.setItem(12,
                    createGuiItem(
                        Material.ELYTRA,
                        1,
                        false,
                        "&eElytra",
                        "&8Upgradable",
                        "",
                        "&c&lMAXED"
                    )
                )
            } else {
                inv!!.setItem(12,
                    createGuiItem(
                        Material.ELYTRA,
                        1,
                        false,
                        "&eElytra",
                        "&8Upgradable",
                        "",
                        "&fCost: &6${formatNumber(elytraCost)}g",
                        "",
                        "&7Click to upgrade!"
                    )
                )
            }

            // Leggings
            if (leggingsCost == -1) {
                inv!!.setItem(13,
                    createGuiItem(
                        Material.LEATHER_LEGGINGS,
                        1,
                        false,
                        "&eLeggings",
                        "&8Upgradable",
                        "",
                        "&c&lMAXED"
                    )
                )
            } else {
                inv!!.setItem(13,
                    createGuiItem(
                        Material.LEATHER_LEGGINGS,
                        1,
                        false,
                        "&eLeggings",
                        "&8Upgradable",
                        "",
                        "&fCost: &6${formatNumber(leggingsCost)}g",
                        "",
                        "&7Click to upgrade!"
                    )
                )
            }

            // Boots
            if (bootsCost == -1) {
                inv!!.setItem(14,
                    createGuiItem(
                        Material.LEATHER_BOOTS,
                        1,
                        false,
                        "&eBoots",
                        "&8Upgradable",
                        "",
                        "&c&lMAXED"
                    )
                )
            } else {
                inv!!.setItem(
                    14,
                    createGuiItem(
                        Material.LEATHER_BOOTS,
                        1,
                        false,
                        "&eBoots",
                        "&8Upgradable",
                        "",
                        "&fCost: &6${formatNumber(bootsCost)}g",
                        "",
                        "&7Click to upgrade!"
                    )
                )
            }

            // Swords
            if (swordCost == -1) {
                inv!!.setItem(
                    15,
                    createGuiItem(
                        Material.WOODEN_SWORD,
                        1,
                        false,
                        "&eSword",
                        "&8Upgradable",
                        "",
                        "&c&lMAXED"
                    )
                )
            } else {
                inv!!.setItem(
                    15,
                    createGuiItem(
                        Material.WOODEN_SWORD,
                        1,
                        false,
                        "&eSword",
                        "&8Upgradable",
                        "",
                        "&fCost: &6${formatNumber(swordCost)}g",
                        "",
                        "&7Click to upgrade!"
                    )
                )
            }

            // Shears
            if (shearsCost == -1) {
                inv!!.setItem(
                    22,
                    createGuiItem(
                        Material.SHEARS,
                        1,
                        false,
                        "&eShears",
                        "&8Upgradable",
                        "",
                        "&c&lMAXED"
                    )
                )
            } else {
                inv!!.setItem(
                    22,
                    createGuiItem(
                        Material.SHEARS,
                        1,
                        false,
                        "&eShears",
                        "&8Upgradable",
                        "",
                        "&fCost: &6${formatNumber(shearsCost)}g",
                        "",
                        "&7Click to upgrade!"
                    )
                )
            }


            inv!!.setItem(
                27,
                createGuiItem(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cClose"
                )
            )
        }
    }
}