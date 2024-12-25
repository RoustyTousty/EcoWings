package me.roustytousty.elytrapvp.services.event

interface EventIntefrace {
    val name: String
    val description: String
    val cost: Int
    val duration: Int
    var isActive: Boolean
    var contributions: Int

    fun activate()
    fun deactivate()
}