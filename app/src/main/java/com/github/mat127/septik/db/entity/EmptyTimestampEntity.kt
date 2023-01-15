package com.github.mat127.septik.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.mat127.septik.model.EmptyTimestamp
import java.time.Instant

@Entity(tableName = "empty_timestamps")
data class EmptyTimestampEntity(
    @PrimaryKey override val timestamp: Instant
) : EmptyTimestamp