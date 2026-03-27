package me.roustytousty.elytrapvp.utility

import org.bukkit.Sound
import org.bukkit.entity.Player


object SoundUtils {



    /*
        Plays a success sound to player
    */
    fun playSuccess(player: Player) {
        player.playSound(player.location, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f)
    }



    /*
        Plays a failure sound to player
    */
    fun playFailure(player: Player) {
        player.playSound(player.location, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f)
    }



    /*
        Plays a ui click sound to player
    */
    fun playGuiClick(player: Player) {
        player.playSound(player.location, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f)
    }
}