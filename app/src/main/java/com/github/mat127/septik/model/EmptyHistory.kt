package com.github.mat127.septik.model

import java.time.LocalDateTime
import java.util.Comparator

class EmptyHistory {

    val history = mutableListOf<LocalDateTime>()

    fun add(timestamp: LocalDateTime) {
        var index = history.binarySearch(timestamp, Comparator.naturalOrder())
        if (index < 0) index = -index - 1
        history.add(index, timestamp)
        changed()
    }

    fun getLastEmptyTimestamp() = history.lastOrNull()

    fun clear() {
        history.clear()
        changed()
    }

    interface Observer {
        fun changed(history: EmptyHistory)
    }
    private val observers = mutableListOf<Observer>()
    fun addObserver(observer: Observer) = observers.add(observer)
    fun removeObserver(observer: Observer) = observers.remove(observer)
    private fun changed() = observers.forEach { it.changed(this) }
}