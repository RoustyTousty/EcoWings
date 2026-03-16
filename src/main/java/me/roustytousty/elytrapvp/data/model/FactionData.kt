package me.roustytousty.elytrapvp.data.model

import java.util.*

data class FactionData(
    val uuid: UUID,
    var name: String,

    var owner: UUID,
    var members: MutableSet<UUID> = mutableSetOf(),

    var factionPoints: Int = 0,
    var lifetimeFactionPoints: Int = 0
)