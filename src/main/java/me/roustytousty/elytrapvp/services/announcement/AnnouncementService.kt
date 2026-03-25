package me.roustytousty.elytrapvp.services.announcement

import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class AnnouncementService(
    private val plugin: JavaPlugin
) {

    private val messages = listOf(
        "&fCheck out the &6/shop &fto buy blocks and utility items!",
        "&fFound a bug? Report it on our &6/discord&f!",
        "&fYou earn gold by staying alive and getting kills!",
        "&fLike the server? Join our community at &6/discord&f!"
    )

    private var currentIndex = 0

    init {
        start()
    }

    private fun start() {
        val interval = 6000L

        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            broadcastNext()
        }, interval, interval)
    }

    private fun broadcastNext() {
        val message = messages[currentIndex]
        MessageUtils.sendMessage(message)

        currentIndex = (currentIndex + 1) % messages.size
    }
}