package me.roustytousty.elytrapvp.services.event

import me.roustytousty.elytrapvp.data.repository.EventRepository
import me.roustytousty.elytrapvp.services.event.events.MoonEvent
import me.roustytousty.elytrapvp.services.event.events.TNTRainEvent
import me.roustytousty.elytrapvp.services.event.events.VoidlessEvent
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class EventService(
    private val eventRepository: EventRepository,
    private val playerService: PlayerService,
    private val plugin: JavaPlugin
) {

    private val events = mutableMapOf<String, EventInterface>()

    private var activeEvent: EventInterface? = null
        private set

    init {
        registerEvent(TNTRainEvent())
        registerEvent(VoidlessEvent())
    }

    private fun registerEvent(event: EventInterface) {
        event.contributions = eventRepository.loadContributions(event.name)
        events[event.name] = event
        println("Registered event named ${event.name} with ${event.contributions}g contributed.")
    }

    fun getEvents(): List<EventInterface> = events.values.toList()

    fun getActiveEvent(): EventInterface? {
        return activeEvent
    }

    fun isAnyEventActive(): Boolean = activeEvent != null

    fun isEventActive(eventName: String): Boolean {
        return activeEvent?.name == eventName
    }

    fun saveAllEventData() {
        for (event in events.values) {
            eventRepository.saveContributions(event.name, event.contributions)
        }
    }

    fun processPlayerContribution(player: Player, eventName: String, amount: Int): Boolean {
        if (isAnyEventActive()) {
            MessageUtils.sendMessage(player, "&fYou cannot contribute to an &6&lEVENT &fwhile one is active!")
            SoundUtils.playFailure(player)
            return false
        }

        val event = events[eventName] ?: return false
        val playerData = playerService.getOrCreatePlayerData(player)

        if (playerData.gold < amount) {
            MessageUtils.sendError(player, "&fYou don't have enough gold!")
            SoundUtils.playFailure(player)
            return false
        }

        playerData.gold -= amount
        event.contributions += amount

        MessageUtils.sendMessage(player, "&fYou contributed &6&l${amount}g &fto the &6&l${eventName} &fevent!")
        SoundUtils.playSuccess(player)

        if (event.contributions >= event.cost) {
            event.contributions -= event.cost
            saveEventAsync(event)
            activateEvent(eventName)
        } else {
            saveEventAsync(event)
        }

        return true
    }

    fun activateEvent(eventName: String) {
        if (isAnyEventActive()) {
            println("Cannot activate $eventName. Another event is already active!")
            return
        }

        val event = events[eventName] ?: return

        event.activate()
        event.endTime = System.currentTimeMillis() + (event.duration * 1000L)
        event.task = startDeactivateTask(event)

        activeEvent = event

        playActivationSound()
        MessageUtils.sendAnnouncement("&fGlobal event activated: &6&l${eventName}&f!")
    }

    fun deactivateEvent(eventName: String) {
        val event = events[eventName]
        if (event != null && isEventActive(eventName)) {
            event.deactivate()
            event.task?.cancel()
            event.task = null
            activeEvent = null

            playActivationSound()
            MessageUtils.sendAnnouncement("&fGlobal event deactivated: &6&l${eventName}&f!")
        } else {
            println("Event $eventName not found or not active!")
        }
    }

    fun getEventRemainingTime(eventName: String): String {
        val event = events[eventName] ?: return "00:00"

        val remainingTimeMillis = event.endTime?.minus(System.currentTimeMillis()) ?: 0L
        if (remainingTimeMillis <= 0 || !isEventActive(eventName)) return "00:00"

        val seconds = (remainingTimeMillis / 1000) % 60
        val minutes = (remainingTimeMillis / 1000) / 60

        return String.format("%02d:%02d", minutes, seconds)
    }


    private fun saveEventAsync(event: EventInterface) {
        Bukkit.getScheduler().runTaskAsynchronously(
            plugin,
            Runnable {
                eventRepository.saveContributions(event.name, event.contributions)
            }
        )
    }

    private fun startDeactivateTask(event: EventInterface): BukkitTask {
        return Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable {
                deactivateEvent(event.name)
            },
            event.duration * 20L
        )
    }

    private fun playActivationSound() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.playSound(player.location, Sound.BLOCK_BEACON_ACTIVATE, 2.0f, 0.8f)
        }
    }
}