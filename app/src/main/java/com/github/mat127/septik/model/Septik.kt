package com.github.mat127.septik.model

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToLong

private val SPEED_CALCULATION_INTERVAL = Duration.ofDays(30)

class Septik {

    val stateHistory = StateHistory()
    val emptyHistory = EmptyHistory()

    val volume = 11.0

    fun getFullDate(): LocalDateTime? {
        val start = emptyHistory.getLastEmptyTimestamp()
        if (start == null) return null
        val speed = stateHistory.getSpeed(SPEED_CALCULATION_INTERVAL)
        if (speed == null) return null
        return start.plus(volume.div(speed).roundToLong(), ChronoUnit.SECONDS)
    }
}