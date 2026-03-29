package me.roustytousty.elytrapvp.utility

import net.luckperms.api.LuckPermsProvider
import org.bukkit.entity.Player

object LuckPermsUtils {
    private val api by lazy { LuckPermsProvider.get() }

    fun getPrefix(player: Player): String {
        val user = api.userManager.getUser(player.uniqueId) ?: return ""
        return user.cachedData.metaData.prefix ?: ""
    }

    fun getWeight(player: Player): Int {
        val lp = LuckPermsProvider.get()
        val user = lp.userManager.getUser(player.uniqueId) ?: return 0
        val groupName = user.primaryGroup
        val group = lp.groupManager.getGroup(groupName) ?: return 0
        return group.weight.orElse(0)
    }
}