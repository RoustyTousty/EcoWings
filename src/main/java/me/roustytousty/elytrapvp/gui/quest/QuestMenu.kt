package me.roustytousty.elytrapvp.gui.quest

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.services.quest.QuestRewardType
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

class QuestMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        if (e.view.title != "Daily Quests") return
        e.isCancelled = true

        val p = e.whoClicked as Player
        val slot = e.rawSlot
        val questIndex = when (slot) {
            11 -> 0
            13 -> 1
            15 -> 2
            else -> -1
        }

        if (questIndex != -1) {
            val quest = Services.questService.getDailyQuests().getOrNull(questIndex) ?: return
            if (Services.questService.claimReward(p, quest.id)) {
                SoundUtils.playGuiClick(p)
                openInventory(p)
            }
        } else if (slot == 18) {
            p.closeInventory()
            SoundUtils.playGuiClick(p)
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Daily Quests") e.isCancelled = true
    }

    companion object {
        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 27, "Daily Quests")
            initItems(player, inventory)
            player.openInventory(inventory)
            SoundUtils.playGuiClick(player)
        }

        private fun initItems(player: Player, inventory: Inventory) {
            val playerData = Services.playerService.getOrCreatePlayerData(player)
            Services.questService.checkAndResetDailyData(playerData)

            val border = intArrayOf(8, 9, 17, 26)
            border.forEach { inventory.setItem(it, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f")) }

            inventory.setItem(0, itemBuilder(Material.CLOCK, 1, false, "&e&lQuest Refresh", "&7Next reset in:", "&6${Services.questService.getTimeUntilRefresh()}"))

            val questSlots = intArrayOf(11, 13, 15)
            Services.questService.getDailyQuests().forEachIndexed { index, quest ->
                val progress = playerData.questProgress.getOrDefault(quest.id, 0)
                val isClaimed = playerData.claimedQuests.contains(quest.id)
                val isDone = progress >= quest.goal

                val lore = mutableListOf("&7${quest.description}", "", "&fProgress: &e$progress&f/&e${quest.goal}")

                val rColor = if (quest.rewardType == QuestRewardType.SHARD) "&b" else "&6"
                val rewardName = if (quest.rewardType == QuestRewardType.SHARD) "shards" else "gold"

                lore.add("&fReward: $rColor${quest.rewardAmount} $rewardName")
                lore.add("")

                val mat: Material
                if (isClaimed) {
                    mat = Material.MINECART
                    lore.add("&c&lALREADY CLAIMED")
                } else if (isDone) {
                    mat = Material.CHEST_MINECART
                    lore.add("&a&lCLICK TO CLAIM")
                } else {
                    mat = Material.HOPPER_MINECART
                    lore.add("&e&lIN PROGRESS...")
                }

                inventory.setItem(questSlots[index], itemBuilder(mat, 1, isDone && !isClaimed, "&eQuest #${index + 1}", *lore.toTypedArray()))
            }
            inventory.setItem(18, itemBuilder(Material.RED_STAINED_GLASS_PANE, 1, false, "&cClose"))
        }
    }
}