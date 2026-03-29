package me.roustytousty.elytrapvp.data.repository

interface EventRepository {

    fun loadContributions(eventName: String): Int

    fun saveContributions(eventName: String, contributions: Int)
}