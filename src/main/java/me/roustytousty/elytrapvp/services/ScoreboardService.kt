package me.roustytousty.elytrapvp.services

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.FormatUtils.formatNumber
import me.roustytousty.elytrapvp.utility.FormatUtils.parse
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot

class ScoreboardService {

    fun create(player: Player) {
        val board = Bukkit.getScoreboardManager().newScoreboard
        val objective = board.registerNewObjective("test", "dummy", parse("  &6EcoWings &7[1.21.x]  "))
        objective.displaySlot = DisplaySlot.SIDEBAR

        val line1 = objective.getScore(parse("&f&lServer:"))
        val line2 = objective.getScore(parse(" &7| &fPlayers: " + Bukkit.getOnlinePlayers().size))
        val line3 = objective.getScore(parse("&f"))
        val line4 = objective.getScore(parse("&f${player.name}"))

        val goldEntry = parse(" &7| &6Gold: ")
        val goldTeam = board.registerNewTeam("gold")
        goldTeam.addEntry(goldEntry)
        goldTeam.suffix = parse("&f${formatNumber(CacheConfig.getplrVal(player, "gold") as Int)}")

        val killsEntry = parse(" &7| &6Kills: ")
        val killsTeam = board.registerNewTeam("kills")
        killsTeam.addEntry(killsEntry)
        killsTeam.suffix = parse("&f${formatNumber(CacheConfig.getplrVal(player, "kills") as Int)}")

        val killstreakEntry = parse(" &7| &6Killstreak: ")
        val killstreakTeam = board.registerNewTeam("killstreak")
        killstreakTeam.addEntry(killstreakEntry)
        killstreakTeam.suffix = parse("&f${formatNumber(CacheConfig.getplrVal(player, "killstreak") as Int)} &8(${formatNumber(CacheConfig.getplrVal(player, "topkillstreak") as Int)})")

        line1.score = 9
        line2.score = 8
        line3.score = 7
        line4.score = 6

        objective.getScore(goldEntry).score = 5
        objective.getScore(killsEntry).score = 4
        objective.getScore(killstreakEntry).score = 3

        val line8 = objective.getScore(parse("&6"))
        val line9 = objective.getScore(parse("&7ecowings.minehut.gg"))
        line8.score = 2
        line9.score = 1

        player.scoreboard = board
    }

    fun startUpdateTask() {
        object : BukkitRunnable() {
            override fun run() {
                update()
            }
        }.runTaskTimer(ElytraPVP.instance!!, 0, 20)
    }

    private fun update() {
        for (player in Bukkit.getOnlinePlayers()) {
            val board = player.scoreboard
            val goldTeam = board.getTeam("gold") ?: continue
            val killsTeam = board.getTeam("kills") ?: continue
            val killstreakTeam = board.getTeam("killstreak") ?: continue

            val gold = CacheConfig.getplrVal(player, "gold") as Int
            goldTeam.suffix = parse("&f${formatNumber(gold)}")

            val kills = CacheConfig.getplrVal(player, "kills") as Int
            killsTeam.suffix = parse("&f${formatNumber(kills)}")

            val killstreak = CacheConfig.getplrVal(player, "killstreak") as Int
            val topkillstreak = formatNumber(CacheConfig.getplrVal(player, "killstreak") as Int)
            killstreakTeam.suffix = parse("&f${formatNumber(killstreak)} &8(${topkillstreak})")
        }
    }
}