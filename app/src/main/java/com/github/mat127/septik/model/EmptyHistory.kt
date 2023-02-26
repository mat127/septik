package com.github.mat127.septik.model

import androidx.annotation.WorkerThread
import com.github.mat127.septik.db.dao.EmptyTimestampsDao
import com.github.mat127.septik.db.entity.EmptyTimestampEntity
import java.time.Duration
import java.time.Instant
import java.time.temporal.TemporalAmount

class EmptyHistory(
    private val dao: EmptyTimestampsDao
) {

    @WorkerThread
    suspend fun add(timestamp: Instant) {
        dao.insert(EmptyTimestampEntity(timestamp))
        changed()
    }

    suspend fun getLastEmptyTimestamp(): Instant? {
        return dao.getLast()?.timestamp
    }

    suspend fun getEmptingCountPerYear(calculationInterval: TemporalAmount): Double? {
        val timestamp = Instant.now().minus(calculationInterval)
        val stats = dao.getStatsSince(timestamp)
        val emptingDuration = stats.getEmptingDuration() ?: return null
        return Duration.ofDays(365).toSeconds().toDouble() / emptingDuration.toSeconds()
    }

    interface Observer {
        fun changed(history: EmptyHistory)
    }
    private val observers = mutableListOf<Observer>()
    fun addObserver(observer: Observer) = observers.add(observer)
    fun removeObserver(observer: Observer) = observers.remove(observer)
    private fun changed() = observers.forEach { it.changed(this) }
}