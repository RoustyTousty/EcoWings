package me.roustytousty.elytrapvp.services.event

import me.roustytousty.elytrapvp.data.repository.EventRepository
import me.roustytousty.elytrapvp.services.event.events.*
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.*

class EventService(
    private val eventRepository: EventRepository,
    private val playerService: PlayerService,
    private val plugin: JavaPlugin
) {

    private val allEvents = mutableMapOf<String, EventInterface>()

    private val displayPool = mutableListOf<EventInterface>()
    private val waitingPool = mutableListOf<EventInterface>()

    private var activeEvent: EventInterface? = null
        private set

    init {
        registerEvent(TNTRainEvent())
        registerEvent(VoidlessEvent())
        registerEvent(KingOfTheBoxEvent())
        registerEvent(HotPotatoEvent())

        setupRotationPool()
        startPassiveContributionTask()
    }

    private fun registerEvent(event: EventInterface) {
        event.contributions = eventRepository.loadContributions(event.name)
        allEvents[event.name] = event
    }

    private fun setupRotationPool() {
        val eventList = allEvents.values.toMutableList()
        eventList.shuffle()

        for (event in eventList) {
            if (displayPool.size < 3) {
                displayPool.add(event)
            } else {
                waitingPool.add(event)
            }
        }
    }

    private fun startPassiveContributionTask() {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (isAnyEventActive()) return@Runnable

            if (displayPool.isEmpty()) return@Runnable

            val randomEvent = displayPool.random()
            randomEvent.contributions += 10

            if (randomEvent.contributions >= randomEvent.cost) {
                randomEvent.contributions = randomEvent.cost
                saveEventAsync(randomEvent)
                activateEvent(randomEvent.name)
            } else {
                saveEventAsync(randomEvent)
            }
        }, 1200L, 1200L)
    }

    fun getDisplayEvents(): List<EventInterface> = displayPool

    fun getActiveEvent(): EventInterface? = activeEvent
    fun isAnyEventActive(): Boolean = activeEvent != null
    fun isEventActive(eventName: String): Boolean = activeEvent?.name == eventName

    fun saveAllEventData() {
        for (event in allEvents.values) {
            eventRepository.saveContributions(event.name, event.contributions)
        }
    }

    fun forceStopActiveEvent() {
        activeEvent?.name?.let { eventName ->
            Bukkit.getLogger().info("Force stopping active event: $eventName due to server shutdown.")
            deactivateEvent(eventName)
        }
    }

    fun processPlayerContribution(player: Player, eventName: String, amount: Int): Boolean {
        if (isAnyEventActive()) {
            MessageUtils.sendMessage(player, "&fYou cannot contribute to an &6&lEVENT &fwhile one is active!")
            SoundUtils.playFailure(player)
            return false
        }

        val event = allEvents[eventName] ?: return false
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
            event.contributions = event.cost
            saveEventAsync(event)
            activateEvent(eventName)
        } else {
            saveEventAsync(event)
        }

        return true
    }

    fun activateEvent(eventName: String) {
        if (isAnyEventActive()) return

        val event = allEvents[eventName] ?: return

        event.activate()
        event.endTime = System.currentTimeMillis() + (event.duration * 1000L)
        event.task = startDeactivateTask(event)
        activeEvent = event

        playActivationSound()
        MessageUtils.sendAnnouncement("&fGlobal event activated: &6&l${eventName}&f!")
    }

    fun deactivateEvent(eventName: String) {
        val event = allEvents[eventName]
        if (event != null && isEventActive(eventName)) {
            event.deactivate()
            event.task?.cancel()
            event.task = null
            activeEvent = null

            event.contributions = 0
            saveEventAsync(event)

            if (displayPool.contains(event) && waitingPool.isNotEmpty()) {
                displayPool.remove(event)
                waitingPool.add(event)

                val nextEvent = waitingPool.removeAt(0)
                displayPool.add(nextEvent)
            }

            playActivationSound()
            MessageUtils.sendAnnouncement("&fGlobal event deactivated: &6&l${eventName}&f!")
        }
    }

    fun getEventRemainingTime(eventName: String): String {
        val event = allEvents[eventName] ?: return "00:00"

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