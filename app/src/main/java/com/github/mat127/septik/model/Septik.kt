package com.github.mat127.septik.model

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToLong

private val SPEED_CALCULATION_INTERVAL = Duration.ofDays(30)

class Septik {

    val history = StateHistory()

    val volume = 11.0

    fun getFullDate(): LocalDateTime? {
        val start = history.getLastEmptyTimestamp()
        if (start == null) return null
        val speed = history.getSpeed(SPEED_CALCULATION_INTERVAL)
        if (speed == null) return null
        return start.plus(volume.div(speed).roundToLong(), ChronoUnit.SECONDS)
    }
}