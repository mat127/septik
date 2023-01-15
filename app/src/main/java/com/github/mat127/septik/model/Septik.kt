package com.github.mat127.septik.model

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
import kotlin.math.roundToLong

private val SPEED_CALCULATION_INTERVAL = Duration.ofDays(30)

class Septik : StateHistory.Observer, EmptyHistory.Observer {

    val stateHistory = StateHistory()
    init {
        stateHistory.addObserver(this)
    }

    val emptyHistory = EmptyHistory()
    init {
        emptyHistory.addObserver(this)
    }

    val volume = 11.0

    val stateNow: Double get() {
        val start = emptyHistory.getLastEmptyTimestamp()
        if (start == null) return Double.NaN
        val speed = stateHistory.getSpeed(SPEED_CALCULATION_INTERVAL)
        if (speed == null) return Double.NaN
        val duration = Duration.between(start, Instant.now())
        return speed * duration.seconds
    }

    fun percent(state: Double): Int  =
        if(state.isNaN()) -1
        else state.div(volume).times(100).roundToInt()

    val nextFullDate: Instant? get() {
        val start = emptyHistory.getLastEmptyTimestamp()
        if (start == null) return null
        val speed = stateHistory.getSpeed(SPEED_CALCULATION_INTERVAL)
        if (speed == null) return null
        return start.plus(volume.div(speed).roundToLong(), ChronoUnit.SECONDS)
    }

    override fun changed(history: EmptyHistory) = changed()

    override fun changed(history: StateHistory) = changed()

    interface Observer {
        fun changed(septik: Septik)
    }
    private val observers = mutableListOf<Observer>()
    fun addObserver(observer: Observer) = observers.add(observer)
    fun removeObserver(observer: Observer) = observers.remove(observer)
    private fun changed() = observers.forEach { it.changed(this) }
}