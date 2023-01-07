package com.github.mat127.septik.model

import java.time.LocalDateTime
import java.util.Comparator

class EmptyHistory {

    val history = mutableListOf<LocalDateTime>()

    fun add(timestamp: LocalDateTime) {
        var index = history.binarySearch(timestamp, Comparator.naturalOrder())
        if (index < 0) index = -index - 1
        history.add(index, timestamp)
    }

    fun getLastEmptyTimestamp() = history.lastOrNull()

    fun clear() = history.clear()
}