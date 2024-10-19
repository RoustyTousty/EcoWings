package me.roustytousty.elytrapvp.gui.stats

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.GuiUtils.createGuiItem
import me.roustytousty.elytrapvp.utility.GuiUtils.createPlayerHead
import me.roustytousty.elytrapvp.utility.StringUtils.formatNumber
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class PlayerStatsMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player
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
            inv = Bukkit.createInventory(null, 36, "Stats - ${player.name}")
            initItems(player)
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(player: Player) {
            val slots = intArrayOf(0, 8, 9, 17, 18, 26, 27, 35)
            for (slot in slots) {
                inv!!.setItem(slot, createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            val gold = CacheConfig.getplrVal(player, "gold")
            val kills = CacheConfig.getplrVal(player, "kills")
            val deaths = CacheConfig.getplrVal(player, "deaths")


            inv!!.setItem(
                13,
                createPlayerHead(
                    player,
                    "&eStats",
                    "&7Your personal stats menu!"
                )
            )
            inv!!.setItem(
                20,
                createGuiItem(
                    Material.GOLDEN_SWORD,
                    1,
                    false,
                    "&eTop Killstreak",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                21,
                createGuiItem(
                    Material.AMETHYST_SHARD,
                    1,
                    false,
                    "&eRebirths",
                    "&7Test!"
                )
            )
            inv!!.setItem(
                22,
                createGuiItem(
                    Material.GOLD_INGOT,
                    1,
                    false,
                    "&eGold",
                    "&7${formatNumber(gold as Int)}"
                )
            )
            inv!!.setItem(
                23,
                createGuiItem(
                    Material.WOODEN_SWORD,
                    1,
                    false,
                    "&eKills",
                    "&7${formatNumber(kills as Int)}"
                )
            )
            inv!!.setItem(
                24,
                createGuiItem(
                    Material.SKELETON_SKULL,
                    1,
                    false,
                    "&eDeaths",
                    "&7${formatNumber(deaths as Int)}"
                )
            )
        }
    }
}