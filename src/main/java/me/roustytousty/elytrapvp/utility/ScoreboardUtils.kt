package me.roustytousty.elytrapvp.utility

import me.roustytousty.elytrapvp.ElytraPVP
import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.StringUtils.formatNumber
import me.roustytousty.elytrapvp.utility.StringUtils.parse
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot

object ScoreboardUtils {

    fun createScoreboard(player: Player) {
        val board = Bukkit.getScoreboardManager().newScoreboard
        val objective = board.registerNewObjective("test", "dummy", parse("  &6WWWings &7[1.21.x]  "))
        objective.displaySlot = DisplaySlot.SIDEBAR

        val line1 = objective.getScore(parse("&f&lServer:"))
        val line2 = objective.getScore(parse(" &7| &fPlayers: "))
        val line3 = objective.getScore(parse("&f"))
        val line4 = objective.getScore(parse("&f${player.name}"))

        val goldTeam = board.registerNewTeam("gold")
        goldTeam.addEntry(parse(" &7| &6Gold: "))
        goldTeam.suffix = parse("&f${formatNumber(CacheConfig.getplrVal(player, "gold") as Int)}")

        val killsTeam = board.registerNewTeam("kills")
        killsTeam.addEntry(parse(" &7| &6Kills: "))
        killsTeam.suffix = parse("&f${formatNumber(CacheConfig.getplrVal(player, "kills") as Int)}")

        val killstreakTeam = board.registerNewTeam("killstreak")
        killstreakTeam.addEntry(parse(" &7| &6Killstreak: "))
        killstreakTeam.suffix = parse("&f${formatNumber(CacheConfig.getplrVal(player, "killstreak") as Int)} &8(0)")

        line1.score = 8
        line2.score = 7
        line3.score = 6
        line4.score = 5

        objective.getScore(parse("&6Gold: ")).score = 4
        objective.getScore(parse("&6Kills: ")).score = 3
        objective.getScore(parse("&6Killstreak: ")).score = 2

        val line8 = objective.getScore(parse("&6"))
        val line9 = objective.getScore(parse("&7wwwings.minehut.gg"))
        line8.score = 1
        line9.score = 0

        player.scoreboard = board
    }

    fun updateScoreboardForAll() {
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
            killstreakTeam.suffix = parse("&f${formatNumber(killstreak)}")
        }
    }

    fun startUpdatingScoreboard() {
        object : BukkitRunnable() {
            override fun run() {
                updateScoreboardForAll()
            }
        }.runTaskTimer(ElytraPVP.instance!!, 0, 50)
    }
}