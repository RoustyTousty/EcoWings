package me.roustytousty.elytrapvp.gui.stats

import me.roustytousty.elytrapvp.api.MongoDB
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.gui.shops.ShopMenu
import me.roustytousty.elytrapvp.utility.FormatUtils.formatNumber
import me.roustytousty.elytrapvp.utility.ItemUtils
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class LeaderboardMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.inventory != inv) return
        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            27 -> LeaderboardSelectMenu.openInventory(p)
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
        fun openInventory(player: Player, stat: String) {
            inv = Bukkit.createInventory(null, 36, "Leaderboard - $stat")
            initItems(player, stat)
            player.openInventory(inv!!)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(player: Player, stat: String) {
            val black_glass_slots = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24)
            for (slot in black_glass_slots) {
                inv!!.setItem(slot,
                    ItemUtils.itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }

            val brown_glass_slots = intArrayOf(6, 24)
            for (slot in brown_glass_slots) {
                inv!!.setItem(slot,
                    ItemUtils.itemBuilder(Material.BROWN_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }

            val white_glass_slots = intArrayOf(7, 25)
            for (slot in white_glass_slots) {
                inv!!.setItem(slot,
                    ItemUtils.itemBuilder(Material.WHITE_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }

            val gold_glass_slots = intArrayOf(8, 26)
            for (slot in gold_glass_slots) {
                inv!!.setItem(slot,
                    ItemUtils.itemBuilder(Material.YELLOW_STAINED_GLASS_PANE, 1, false, "&f")
                )
            }

            val playerStat = CacheConfig.getplrVal(player, stat)
            val playerRank = MongoDB.getPlayerRank(player, stat)
            inv!!.setItem(35,
                ItemUtils.itemBuilder(player, 1, false, "&e${player.name} &6#$playerRank", "&7Value: ${formatNumber(playerStat as Int)}")
            )

            val leaderboardSlots = intArrayOf(17, 16, 15, 14, 13, 12, 11, 10, 9)
            val topPlayers = MongoDB.getTopPlayers(stat, 9)

            topPlayers.forEachIndexed { index, playerDoc ->
                val pName = playerDoc.getString("username")
                val pStat = playerDoc.getInteger(stat)
                if (pStat != null) {
                    val skullItem = ItemUtils.itemBuilder(Bukkit.getOfflinePlayer(pName), 1, false, "&e$pName &6#${index + 1}", "&7Value: ${formatNumber(pStat as Int)}")

                    inv!!.setItem(leaderboardSlots[index], skullItem)
                }
            }

            inv!!.setItem(27,
                ItemUtils.itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cBack")
            )
        }
    }
}