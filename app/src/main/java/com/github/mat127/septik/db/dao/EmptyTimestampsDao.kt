package com.github.mat127.septik.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.mat127.septik.db.entity.EmptingStats
import com.github.mat127.septik.db.entity.EmptyTimestampEntity
import java.time.Instant

@Dao
interface EmptyTimestampsDao {

    @Insert
    suspend fun insert(empty: EmptyTimestampEntity)

    @Query("SELECT timestamp FROM empty_timestamps ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLast(): EmptyTimestampEntity?

    @Query("SELECT MIN(timestamp) AS since, MAX(timestamp) AS till, COUNT(*) AS empting_count FROM empty_timestamps WHERE timestamp >= :timestamp")
    suspend fun getStatsSince(timestamp: Instant): EmptingStats
}