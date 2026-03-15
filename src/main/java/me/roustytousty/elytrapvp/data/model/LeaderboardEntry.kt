package me.roustytousty.elytrapvp.data.model

import java.util.*

data class LeaderboardEntry(
    val uuid: UUID,
    val username: String,
    val value: Int
)