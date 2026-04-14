package me.roustytousty.elytrapvp.services.punishment

import com.destroystokyo.paper.profile.PlayerProfile
import io.papermc.paper.ban.BanListType
import org.bukkit.Bukkit
import org.bukkit.ban.ProfileBanList
import org.bukkit.command.CommandSender
import java.util.Date
import java.util.UUID

class PunishmentService {

    private val ONE_HOUR = 3600000L
    private val ONE_MONTH = 2592000000L

    private val mutes = mutableMapOf<UUID, Long>()

    fun parseDuration(timeStr: String): Long? {
        val regex = "^(\\d+)([smhd]|mo)$".toRegex()
        val match = regex.find(timeStr.lowercase()) ?: return null
        val value = match.groupValues[1].toLong()
        val unit = match.groupValues[2]

        return when (unit) {
            "s" -> value * 1000
            "m" -> value * 60 * 1000
            "h" -> value * 60 * 60 * 1000
            "d" -> value * 24 * 60 * 60 * 1000
            "mo" -> value * 30 * 24 * 60 * 60 * 1000
            else -> null
        }
    }

    fun getMaxDurationMillis(sender: CommandSender): Long {
        if (sender.hasPermission("group.admin") || sender.isOp) return Long.MAX_VALUE
        if (sender.hasPermission("group.mod")) return ONE_MONTH
        if (sender.hasPermission("group.guide")) return ONE_HOUR
        return 0L
    }

    fun isMuted(uuid: UUID): Boolean {
        val expiry = mutes[uuid] ?: return false
        if (System.currentTimeMillis() > expiry) {
            mutes.remove(uuid)
            return false
        }
        return true
    }

    fun mutePlayer(uuid: UUID, durationMillis: Long) {
        val expiryTime = if (durationMillis == Long.MAX_VALUE) Long.MAX_VALUE else System.currentTimeMillis() + durationMillis
        mutes[uuid] = expiryTime
    }

    fun banPlayer(targetName: String, durationMillis: Long, reason: String, source: String) {
        val expiryDate = if (durationMillis == Long.MAX_VALUE) null else Date(System.currentTimeMillis() + durationMillis)
        val profile: org.bukkit.profile.PlayerProfile = Bukkit.createPlayerProfile(targetName)
        val banList: ProfileBanList = Bukkit.getBanList(BanListType.PROFILE)
        val _entry: org.bukkit.BanEntry<PlayerProfile>? = banList.addBan(profile, reason, expiryDate, source)
    }
}