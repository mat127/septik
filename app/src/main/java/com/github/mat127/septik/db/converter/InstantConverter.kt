package com.github.mat127.septik.db.converter

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochSecond(it) }
    }

    @TypeConverter
    fun toTimestamp(value: Instant?): Long? {
        return value?.epochSecond
    }
}