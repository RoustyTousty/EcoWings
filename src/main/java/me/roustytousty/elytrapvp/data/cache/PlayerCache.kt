package me.roustytousty.elytrapvp.data.cache

import me.roustytousty.elytrapvp.data.model.PlayerData
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object PlayerCache {

    private val cache = ConcurrentHashMap<UUID, PlayerData>()

    fun get(uuid: UUID): PlayerData? = cache[uuid]

    fun put(data: PlayerData) {
        cache[data.uuid] = data
    }

    fun remove(uuid: UUID) {
        cache.remove(uuid)
    }

    fun getAll(): Collection<PlayerData> {
        return cache.values
    }
}