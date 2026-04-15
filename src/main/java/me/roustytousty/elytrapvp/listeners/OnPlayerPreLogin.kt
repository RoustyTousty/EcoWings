package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.services.Services
import me.roustytousty.elytrapvp.utility.FormatUtils
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class OnPlayerPreLogin : Listener {

    @EventHandler
    fun onPreLogin(event: AsyncPlayerPreLoginEvent) {
        val uuid = event.uniqueId
        val activeBan = Services.punishmentService.getActiveBan(uuid)

        if (activeBan != null) {
            val timeStr = FormatUtils.formatDuration(activeBan.durationMillis)
            val kickMessage = """
                
                &cYou are banned from this server!
                
                &fPunisher: &6${activeBan.issuer}
                &fDuration: &6$timeStr
                &fReason: &7${activeBan.reason}
                
            """.trimIndent()

            event.disallow(
                AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
                LegacyComponentSerializer.legacyAmpersand().deserialize(kickMessage)
            )
        }
    }
}