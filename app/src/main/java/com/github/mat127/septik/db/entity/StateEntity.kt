package com.github.mat127.septik.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.mat127.septik.model.State
import java.time.Instant

@Entity(tableName = "state_history")
data class StateEntity(
    @PrimaryKey override val timestamp: Instant,
    override val state: Double
): State