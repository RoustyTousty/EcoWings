package me.roustytousty.elytrapvp.utility

import net.luckperms.api.LuckPermsProvider
import org.bukkit.entity.Player

object LuckPermsUtils {
    private val api by lazy { LuckPermsProvider.get() }

    fun getPrefix(player: Player): String {
        val user = api.userManager.getUser(player.uniqueId) ?: return ""
        return user.cachedData.metaData.prefix ?: ""
    }

    fun getGroupName(player: Player): String {
        val user = api.userManager.getUser(player.uniqueId) ?: return "Member"
        val group = api.groupManager.getGroup(user.primaryGroup)
        return group?.displayName ?: user.primaryGroup.replaceFirstChar { it.uppercase() }
    }
}