package me.roustytousty.elytrapvp.services.event

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.scheduler.BukkitTask

class EventService {

    private val events = mutableListOf<EventIntefrace>()
    private val activeEvents = mutableListOf<EventIntefrace>()

    fun registerEvent(event: EventIntefrace) {
        println("Registered event named ${event.name}")
        events.add(event)
    }

    fun getEvents(): List<EventIntefrace> = events

    fun getActiveEvents(): List<EventIntefrace> = activeEvents

    fun isAnyEventActive(): Boolean = activeEvents.isNotEmpty()

    fun isEventActive(eventName: String): Boolean {
        val event = findEventInstance(eventName)
        return activeEvents.contains(event)
    }

    fun activateEvent(eventName: String) {

        if (isEventActive(eventName)) {
            println("Event $eventName is already active!")
            return
        }

        val event = findEventInstance(eventName)
        println(event)
        println(events)
        println(activeEvents)
        if (event != null) {
            event.activate()
            event.endTime = System.currentTimeMillis() + (event.duration * 1000L)
            event.task = startDeactivateTask(event)
            activeEvents.add(event)
            playActivationSound()
            MessageUtils.sendMessage("&fGlobal event activated: &6&l${eventName}&f!")
        } else {
            println("Event $eventName not found!")
        }
    }

    fun deactivateEvent(eventName: String) {
        val event = findEventInstance(eventName)
        if (event != null) {
            event.deactivate()
            event.task!!.cancel()
            activeEvents.remove(event)
            playActivationSound()
            MessageUtils.sendMessage("&fGlobal event deactivated: &6&l${eventName}&f!")
        } else {
            println("Event $eventName not found or not active!")
        }
    }

//    fun deactivateAll() {
//        for ((eventName, task) in activeEvents) {
//            val event = findEventInstance(eventName)
//            if (event != null) {
//                event.deactivate()
//                task.cancel()
//            }
//        }
//        activeEvents.clear()
//    }

    fun contributeToEvent(eventName: String, amount: Int) {
        val event = findEventInstance(eventName)
        if (event != null && !isEventActive(eventName)) {
            event.contributions += amount
            if (event.contributions >= event.cost) {
                event.contributions -= event.cost
                activateEvent(eventName)
            }
        } else {
            println("Event $eventName not found or already active!")
        }
    }

    fun getEventRemainingTime(eventName: String): String {
        val event = findEventInstance(eventName) ?: return "00:00"

        val remainingTimeMillis = event.endTime?.minus(System.currentTimeMillis()) ?: 0L

        if (remainingTimeMillis <= 0) return "00:00"

        val seconds = (remainingTimeMillis / 1000) % 60
        val minutes = (remainingTimeMillis / 1000) / 60

        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun findEventInstance(eventName: String): EventIntefrace? {
        return events.find { it.name == eventName }
    }

    private fun startDeactivateTask(event: EventIntefrace): BukkitTask {
        return Bukkit.getScheduler().runTaskLater(
            ElytraPVP.instance!!,
            Runnable {
                deactivateEvent(event.name)
            },
            event.duration * 20L
        )
    }

    private fun playActivationSound() {
        for (player in Bukkit.getOnlinePlayers()) {
            player.playSound(player, Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f)
        }
    }
}