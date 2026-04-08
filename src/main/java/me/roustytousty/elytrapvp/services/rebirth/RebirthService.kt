package me.roustytousty.elytrapvp.services.rebirth

import me.roustytousty.elytrapvp.data.model.PlayerData
import me.roustytousty.elytrapvp.services.kit.KitService
import me.roustytousty.elytrapvp.services.player.PlayerService
import me.roustytousty.elytrapvp.services.upgrade.UpgradeService
import me.roustytousty.elytrapvp.services.upgrade.UpgradeType
import me.roustytousty.elytrapvp.utility.MessageUtils
import me.roustytousty.elytrapvp.utility.SoundUtils
import org.bukkit.entity.Player

class RebirthService(
    private val playerService: PlayerService,
    private val upgradeService: UpgradeService,
    private val kitService: KitService
) {

    private val BASE_GOLD_COST = 5000
    private val BASE_SHARD_REWARD = 5

    fun getRebirthCost(rebirths: Int): Int {
        return (BASE_GOLD_COST * (1.0 + (rebirths * 0.2))).toInt()
    }

    fun getShardReward(rebirths: Int): Int {
        return (BASE_SHARD_REWARD * (1.0 + (rebirths * 0.2))).toInt()
    }

    fun tryPlayerRebirth(player: Player) {
        val playerData = playerService.getOrCreatePlayerData(player)
        val cost = getRebirthCost(playerData.rebirths)
        val shardReward = getShardReward(playerData.rebirths)

        if (playerData.gold < cost || !isRebirthAllowed(playerData)) {
            MessageUtils.sendError(player, "&fYou don't meet the requirements! (Maxed upgrades & &6$cost Gold&f)")
            SoundUtils.playFailure(player)
            return
        }

        setupPlayerDataOnRebirth(playerData, shardReward)
        kitService.syncKit(player)

        MessageUtils.sendSuccess(player, "&fSuccessfully &6&lRE-BIRTHED&f! You are now rebirth &6#${playerData.rebirths}")
        MessageUtils.sendAnnouncement("&fCongratulate &6${player.name} &fon &6&lRE-BIRTHING&f! Rebirth &6#${playerData.rebirths}&f!")
        SoundUtils.playSuccess(player)
    }

    private fun setupPlayerDataOnRebirth(playerData: PlayerData, shardReward: Int) {
        for (type in UpgradeType.values()) {
            type.setLevel(playerData, 0)
        }

        playerData.gold = 0
        playerData.rebirths += 1
        playerData.shards += shardReward
    }

    private fun isRebirthAllowed(playerData: PlayerData): Boolean {
        return UpgradeType.values().all { type ->
            upgradeService.getNextUpgradeData(playerData, type).maxed
        }
    }
}