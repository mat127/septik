package com.github.mat127.septik.model

import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalAmount

class StateHistory {

    val history = sortedMapOf<Instant,Double>(Comparator.naturalOrder())

    fun add(timestamp: Instant, state: Double) {
        history.put(timestamp, state)
        changed()
    }

    fun getSpeed(speedCalculationInterval: TemporalAmount): Double? {
        if(history.size < 2) return null
        val after = history.entries.last()
        val timestamp = Instant.now().minus(speedCalculationInterval)
        val before = history.filterKeys { it.isBefore(timestamp) && it.isBefore(after.key) }
            .entries.lastOrNull() ?: history.entries.first()
        val volume = after.value - before.value
        val duration = Duration.between(before.key, after.key)
        return volume / duration.seconds
    }

    fun clear() {
        history.clear()
        changed()
    }

    interface Observer {
        fun changed(history: StateHistory)
    }
    private val observers = mutableListOf<Observer>()
    fun addObserver(observer: Observer) = observers.add(observer)
    fun removeObserver(observer: Observer) = observers.remove(observer)
    private fun changed() = observers.forEach { it.changed(this) }
}