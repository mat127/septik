package com.github.mat127.septik.model

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount

class StateHistory {

    val history = sortedMapOf<LocalDateTime,Double>(Comparator.naturalOrder())

    fun add(timestamp: LocalDateTime, state: Double) = history.put(timestamp, state)

    fun getSpeed(speedCalculationInterval: TemporalAmount): Double? {
        if(history.size < 2) return null
        val after = history.entries.last()
        val timestamp = LocalDateTime.now().minus(speedCalculationInterval)
        val before = history.filterKeys { it.isBefore(timestamp) }
            .entries.lastOrNull() ?: history.entries.first()
        val volume = after.value - before.value
        val duration = Duration.between(before.key, after.key)
        return volume / duration.seconds
    }

    fun clear() = history.clear()
}