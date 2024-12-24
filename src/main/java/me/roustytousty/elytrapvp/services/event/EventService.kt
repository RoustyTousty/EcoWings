package me.roustytousty.elytrapvp.services.event

class EventService {

    private val events = mutableListOf<EventIntefrace>()
    private var activeEvent: EventIntefrace? = null

    fun registerEvent(event: EventIntefrace) {
        events.add(event)
    }

    fun getEvents(): List<EventIntefrace> = events

    fun activateEvent(eventName: String) {
        activeEvent?.deactivate()
        val event = events.find { it.name == eventName }
        if (event != null) {
            event.activate()
            activeEvent = event
        }
    }

    fun deactivateEvent(eventName: String) {
        val event = events.find { it.name == eventName }
        event?.deactivate() ?: println("Event $eventName not found!")
    }
}