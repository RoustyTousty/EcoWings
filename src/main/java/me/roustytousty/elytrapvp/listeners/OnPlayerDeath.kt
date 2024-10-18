package me.roustytousty.elytrapvp.listeners

import me.roustytousty.elytrapvp.data.CacheConfig
import me.roustytousty.elytrapvp.utility.StringUtils.parse
import net.kyori.adventure.text.Component
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class OnPlayerDeath : Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val victim = event.entity
        val killer = event.entity.killer

        if (killer != null) {
            val killerKills = (CacheConfig.getplrVal(killer, "kills") as? Int)!!
            val killerKillstreak = (CacheConfig.getplrVal(killer, "killstreak") as? Int)!!
            val killerGold = (CacheConfig.getplrVal(killer, "gold") as? Int)!!

            CacheConfig.setplrVal(killer, "kills", killerKills + 1)
            CacheConfig.setplrVal(killer, "killstreak", killerKillstreak + 1)
            CacheConfig.setplrVal(killer, "gold", killerGold + 10)

            killer.health += 3

            killer.sendActionBar(parse("&6+10g"))

            killer.playSound(killer, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)

            event.deathMessage(Component.text(parse("&6${victim.name} &fwas killed by &6${killer.name}&f!")))
        } else {
            event.deathMessage(Component.text(parse("&6${victim.name} &fhas died!")))
        }


        val victimDeaths = (CacheConfig.getplrVal(victim, "deaths") as? Int)!!

        CacheConfig.setplrVal(victim, "deaths", victimDeaths + 1)
        CacheConfig.setplrVal(victim, "killstreak", 0)
    }
}