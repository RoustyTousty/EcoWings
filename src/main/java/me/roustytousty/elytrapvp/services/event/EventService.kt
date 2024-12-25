package me.roustytousty.elytrapvp.services.event

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask

class EventService {

    private val events = mutableListOf<EventIntefrace>()
    private val activeEvents = mutableMapOf<String, BukkitTask>()

    fun registerEvent(event: EventIntefrace) {
        events.add(event)
    }

    fun getEvents(): List<EventIntefrace> = events

    fun getActiveEvents(): List<String> = activeEvents.keys.toList()

    fun isAnyEventActive(): Boolean = activeEvents.isNotEmpty()

    fun isEventActive(eventName: String): Boolean = activeEvents.containsKey(eventName)

    fun activateEvent(eventName: String) {
        if (activeEvents.containsKey(eventName)) {
            println("Event $eventName is already active!")
            return
        }

        val event = findEventInstance(eventName)
        println(events)
        if (event != null) {
            event.activate()
            activeEvents[eventName] = startDeactivateTask(event)
            MessageUtils.sendMessage("&fGlobal event activated: &6&l${eventName}&f!")
        } else {
            println("Event $eventName not found!")
        }
    }

    fun deactivateEvent(eventName: String) {
        val event = findEventInstance(eventName)
        if (event != null) {
            event.deactivate()
            activeEvents[eventName]?.cancel()
            activeEvents.remove(eventName)
            MessageUtils.sendMessage("&fGlobal event deactivated: &6&l${eventName}&f!")
        } else {
            println("Event $eventName not found or not active!")
        }
    }

    fun deactivateAll() {
        for ((eventName, task) in activeEvents) {
            val event = findEventInstance(eventName)
            if (event != null) {
                event.deactivate()
                task.cancel()
            }
        }
        activeEvents.clear()
    }

    fun contributeToEvent(eventName: String, amount: Int) {
        val event = findEventInstance(eventName)
        if (event != null && !activeEvents.containsKey(eventName)) {
            event.contributions += amount
            if (event.contributions >= event.cost) {
                event.contributions -= event.cost
                activateEvent(eventName)
            }
        } else {
            println("Event $eventName not found or already active!")
        }
    }

    private fun findEventInstance(eventName: String): EventIntefrace? {
        return events.find { it.name == eventName }
    }

    private fun startDeactivateTask(event: EventIntefrace): BukkitTask {
        return Bukkit.getScheduler().runTaskLater(
            ElytraPVP.instance!!,
            Runnable {
                event.deactivate()
                activeEvents.remove(event.name)
                MessageUtils.sendMessage("&fGlobal event deactivated: &6&l${event.name}&f!")
            },
            event.duration * 20L
        )
    }
}