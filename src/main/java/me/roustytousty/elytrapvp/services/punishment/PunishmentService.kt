package me.roustytousty.elytrapvp.services.punishment

import me.roustytousty.elytrapvp.data.model.PunishmentEntry
import me.roustytousty.elytrapvp.data.model.PunishmentType
import me.roustytousty.elytrapvp.services.Services
import org.bukkit.command.CommandSender
import java.util.UUID

class PunishmentService {

    var isChatMuted: Boolean = false

    private val ONE_HOUR = 3600000L
    private val ONE_MONTH = 2592000000L

    fun parseDuration(timeStr: String): Long? {
        val regex = "^(\\d+)(s|sec|m|min|h|d|mo|y)$".toRegex()
        val match = regex.find(timeStr.lowercase()) ?: return null

        val value = match.groupValues[1].toLong()
        val unit = match.groupValues[2]

        return when (unit) {
            "s" -> value * 1000L
            "m", "min" -> value * 60 * 1000L
            "h" -> value * 60 * 60 * 1000L
            "d" -> value * 24 * 60 * 60 * 1000L
            "mo" -> value * 30 * 24 * 60 * 60 * 1000L
            else -> null
        }
    }

    fun getMaxDurationMillis(sender: CommandSender): Long {
        if (sender.hasPermission("group.admin") || sender.isOp) return Long.MAX_VALUE
        if (sender.hasPermission("group.mod")) return ONE_MONTH
        if (sender.hasPermission("group.guide")) return ONE_HOUR
        return 0L
    }

    fun getActiveMute(uuid: UUID): PunishmentEntry? {
        val data = Services.playerService.getPlayerDataByUUID(uuid) ?: return null
        return data.punishments
            .filter { it.type == PunishmentType.MUTE && !it.isExpired() }
            .maxByOrNull { it.timestamp }
    }

    fun getActiveBan(uuid: UUID): PunishmentEntry? {
        val data = Services.playerService.getPlayerDataByUUID(uuid) ?: return null
        return data.punishments
            .filter { it.type == PunishmentType.BAN && !it.isExpired() }
            .maxByOrNull { it.timestamp }
    }

    fun unbanPlayer(uuid: UUID): Boolean {
        val data = Services.playerService.getPlayerDataByUUID(uuid) ?: return false
        val activeBan = data.punishments.filter { it.type == PunishmentType.BAN && !it.isExpired() }.maxByOrNull { it.timestamp }
            ?: return false

        val newDuration = System.currentTimeMillis() - activeBan.timestamp
        val updatedBan = activeBan.copy(durationMillis = newDuration)

        data.punishments.remove(activeBan)
        data.punishments.add(updatedBan)
        Services.playerService.savePlayerData(data)
        return true
    }

    fun unmutePlayer(uuid: UUID): Boolean {
        val data = Services.playerService.getPlayerDataByUUID(uuid) ?: return false
        val activeMute = data.punishments.filter { it.type == PunishmentType.MUTE && !it.isExpired() }.maxByOrNull { it.timestamp }
            ?: return false

        val newDuration = System.currentTimeMillis() - activeMute.timestamp
        val updatedMute = activeMute.copy(durationMillis = newDuration)

        data.punishments.remove(activeMute)
        data.punishments.add(updatedMute)
        Services.playerService.savePlayerData(data)
        return true
    }

    fun punishPlayer(uuid: UUID, type: PunishmentType, durationMillis: Long, reason: String, issuer: String) {
        val entry = PunishmentEntry(type, reason, issuer, System.currentTimeMillis(), durationMillis)

        val data = Services.playerService.getPlayerDataByUUID(uuid) ?: return

        data.punishments.add(entry)
        Services.playerService.savePlayerData(data)
    }
}