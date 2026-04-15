package me.roustytousty.elytrapvp.data.model

data class PunishmentEntry(
    val type: PunishmentType,
    val reason: String,
    val issuer: String,
    val timestamp: Long = System.currentTimeMillis(),
    val durationMillis: Long
) {
    fun isPermanent(): Boolean = durationMillis == Long.MAX_VALUE

    fun isExpired(): Boolean {
        if (isPermanent()) return false
        return System.currentTimeMillis() >= (timestamp + durationMillis)
    }

    fun getExpirationDate(): java.util.Date? {
        if (isPermanent()) return null
        return java.util.Date(timestamp + durationMillis)
    }
}