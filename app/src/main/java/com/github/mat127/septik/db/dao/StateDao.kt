package com.github.mat127.septik.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.github.mat127.septik.db.entity.StateEntity
import java.time.Instant

@Dao
interface StateDao {

    @Insert
    suspend fun insert(state: StateEntity)

    @Query("SELECT * FROM state_history ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLast(): StateEntity?

    @Query("SELECT * FROM state_history WHERE timestamp < :timestamp ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastBefore(timestamp: Instant): StateEntity?
}