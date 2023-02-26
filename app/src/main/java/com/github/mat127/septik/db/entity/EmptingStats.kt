package com.github.mat127.septik.db.entity

import androidx.room.ColumnInfo
import java.time.Duration
import java.time.Instant

data class EmptingStats(
    val since: Instant?,
    val till: Instant?,
    @ColumnInfo(name = "empting_count") val emptingCount: Long
) {
    fun getEmptingDuration(): Duration? {
        if (emptingCount < 2) return null
        val total = Duration.between(since, till)
        return total.dividedBy(emptingCount - 1L)
    }
}
