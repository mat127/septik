package com.github.mat127.septik.model

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount

class StateHistory {

    val history = sortedMapOf<LocalDateTime,Double>(Comparator.reverseOrder())

    fun add(timestamp: LocalDateTime, state: Double) = history.put(timestamp, state)

    fun addEmpty(timestamp: LocalDateTime) = history.put(timestamp, -1.0)

    fun getLastEmptyTimestamp(): LocalDateTime? {
        return history.filterValues { it <= 0.0 }
            .keys.first()
    }

    fun getSpeed(speedCalculationInterval: TemporalAmount): Double? {
        val after = history.entries.firstOrNull()
        val timestamp = LocalDateTime.now().minus(speedCalculationInterval)
        val before = history.filterKeys { it.isBefore(timestamp) }
            .entries.firstOrNull() ?: history.entries.lastOrNull()
        if(after == null || before == null) return null
        val volume = after.value - before.value
        val duration = Duration.between(before.key, after.key)
        return volume / duration.seconds
    }
}