package me.roustytousty.elytrapvp.gui.events

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.gui.upgrade.ConfirmUpgradeMenu
import me.roustytousty.elytrapvp.utility.ItemUtils.itemBuilder
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory

class EventsMenu : Listener {

    @EventHandler
    private fun onInventoryClick(e: InventoryClickEvent) {
        val inventory = e.view.title
        if (inventory != "Events") return

        e.isCancelled = true
        val clickedItem = e.currentItem
        if (clickedItem == null || clickedItem.type.isAir) return
        val p = e.whoClicked as Player

        when (e.rawSlot) {
            11, 13, 15 -> {

                if (eventService.isAnyEventActive()) {
                    p.closeInventory()
                    MessageUtils.sendMessage(p, "&fYou cannot contribute to an &6&lEVENT &fwhile one is active!")
                    return
                }

                val eventName = ChatColor.stripColor(clickedItem.itemMeta?.displayName ?: return) ?: return
                val playerGold = CacheConfig.getplrVal(p, "gold") as? Int ?: 0

                val donationAmount = if (e.isShiftClick) 100 else 10
                if (playerGold >= donationAmount) {
                    MessageUtils.sendMessage(p, "&fYou contributed &6&l${donationAmount}g &fto &6&l${eventName} &fevent!")
                    p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)

                    CacheConfig.setplrVal(p, "gold", playerGold - donationAmount)
                    eventService.contributeToEvent(eventName, donationAmount)

                    openInventory(p)
                } else {
                    MessageUtils.sendError(p, "&fYou don't have enough gold!")
                    p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f)
                }
            }

            18 -> {
                p.closeInventory()
                p.playSound(p, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
            }
        }
    }

    @EventHandler
    private fun onInventoryDrag(e: InventoryDragEvent) {
        if (e.view.title == "Events") {
            e.isCancelled = true
        }
    }

    companion object {

        private val eventService = ElytraPVP.instance!!.getEventService()
        fun openInventory(player: Player) {
            val inventory = Bukkit.createInventory(null, 27, "Events")
            initItems(inventory)
            player.openInventory(inventory)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
        }

        private fun initItems(inventory: Inventory) {
            val slots = intArrayOf(0, 8, 9, 17, 26)
            for (slot in slots) {
                inventory.setItem(slot, itemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1, false, "&f"))
            }

            val eventSlots = intArrayOf(11, 13, 15)
            val events = eventService.getEvents()
            events.forEachIndexed { index, event ->

                inventory.setItem(
                    eventSlots[index],
                    itemBuilder(
                        event.displayMaterial,
                        1,
                        false,
                        "&e${event.name}",
                        "&7${event.description}",
                        "",
                        "&fProgress: &6${event.contributions}g&f/&6${event.cost}g",
                        "",
                        "&7Click to donate 10g!",
                        "&7Shift-Click to donate 100g!"
                    )
                )
            }

            inventory.setItem(
                18,
                itemBuilder(
                    Material.RED_STAINED_GLASS_PANE,
                    1,
                    false,
                    "&cClose"
                )
            )
        }
    }
}