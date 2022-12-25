package com.github.mat127.septik.model

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount

class StateHistory {

    val history = sortedMapOf<LocalDateTime,Double>(Comparator.reverseOrder())

    fun add(timestamp: LocalDateTime, state: Double) = history.put(timestamp, state)

    fun addEmpty(timestamp: LocalDateTime) = history.put(timestamp, -1.0)

    val stateHistory: Map<LocalDateTime,Double>
        get() = history.filterValues { it > 0.0 }

    val emptyHistory: Map<LocalDateTime,Double>
        get() = history.filterValues { it <= 0.0 }

    fun getLastEmptyTimestamp(): LocalDateTime? {
        return emptyHistory.keys.firstOrNull()
    }

    fun getSpeed(speedCalculationInterval: TemporalAmount): Double? {
        if(stateHistory.size < 2) return null
        val after = stateHistory.entries.first()
        val timestamp = LocalDateTime.now().minus(speedCalculationInterval)
        val before = stateHistory.filterKeys { it.isBefore(timestamp) }
            .entries.firstOrNull() ?: stateHistory.entries.last()
        val volume = after.value - before.value
        val duration = Duration.between(before.key, after.key)
        return volume / duration.seconds
    }

    fun clear() = history.clear()
}