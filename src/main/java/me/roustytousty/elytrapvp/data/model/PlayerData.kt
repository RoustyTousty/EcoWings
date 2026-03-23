package me.roustytousty.elytrapvp.data.model

import java.util.*

data class PlayerData(
    val uuid: UUID,
    var username: String,

    var isBuildMode: Boolean = false,

    var gold: Int = 50,
    var rebirthTokens: Int = 0,

    var rebirths: Int = 0,
    var kills: Int = 0,
    var deaths: Int = 0,
    var killstreak: Int = 0,
    var recordKillstreak: Int = 0,

    var helmetLevel: Int = 0,
    var elytraLevel: Int = 0,
    var leggingsLevel: Int = 0,
    var bootsLevel: Int = 0,
    var swordLevel: Int = 0,
    var shearsLevel: Int = 0,
    var pickaxeLevel: Int = 0,
    var axeLevel: Int = 0
)