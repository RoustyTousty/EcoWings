package me.roustytousty.elytrapvp.services.announcement

import me.roustytousty.elytrapvp.utility.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class AnnouncementService(
    private val plugin: JavaPlugin
) {

    private val messages = listOf(
        "&fCheck out &6/shop &ffor blocks and utility items!",
        "&fFound a bug? Report it on our &6/discord&f!",
        "&fTIP you earn gold by &6mining &fgold or by getting &6kills&f!",
        "&fLike the server? Join our community at &6/discord&f!",
        "&fTIP You can get armor and tools in &6/upgrade &fmenu!",
        "&fHave a suggestion? Tell us on &6/discord &fand earn gold by doing so!"
    )

    private var currentIndex = 0

    init {
        start()
    }

    private fun start() {
        val interval = 5000L

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