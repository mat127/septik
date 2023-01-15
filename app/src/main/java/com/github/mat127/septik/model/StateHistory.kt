package com.github.mat127.septik.model

import androidx.annotation.WorkerThread
import com.github.mat127.septik.db.dao.StateDao
import com.github.mat127.septik.db.entity.StateEntity
import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalAmount

class StateHistory(
    private val dao: StateDao
) {

    @WorkerThread
    suspend fun add(timestamp: Instant, state: Double) {
        dao.insert(StateEntity(timestamp, state))
        changed()
    }

    suspend fun getLast() = dao.getLast()

    suspend fun getLastBefore(timestamp: Instant) = dao.getLastBefore(timestamp)

    suspend fun getSpeed(speedCalculationInterval: TemporalAmount): Double? {
        val last = getLast() ?: return null
        var timestamp = Instant.now().minus(speedCalculationInterval)
        if (timestamp.isAfter(last.timestamp)) {
            timestamp = last.timestamp
        }
        var before = getLastBefore(timestamp) ?: return null
        val duration = Duration.between(before.timestamp, last.timestamp)
        val volume = last.state - before.state
        return volume / duration.seconds
    }

    interface Observer {
        fun changed(history: StateHistory)
    }
    private val observers = mutableListOf<Observer>()
    fun addObserver(observer: Observer) = observers.add(observer)
    fun removeObserver(observer: Observer) = observers.remove(observer)
    private fun changed() = observers.forEach { it.changed(this) }
}