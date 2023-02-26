package com.github.mat127.septik.model

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt
import kotlin.math.roundToLong

private val SPEED_CALCULATION_INTERVAL = Duration.ofDays(30) // TODO allow user setup in preferences
private val EMPTING_CALCULATION_INTERVAL = Duration.ofDays(365) // TODO allow user setup in preferences

class Septik(
    val stateHistory: StateHistory,
    val emptyHistory: EmptyHistory
) : StateHistory.Observer, EmptyHistory.Observer {

    init {
        stateHistory.addObserver(this)
        emptyHistory.addObserver(this)
    }

    val volume = 11.0 // TODO allow user setup in preferences

    val emptingPrice = 2475.0 // TODO allow user setup in preferences

    suspend fun getFillingSpeed() =
        stateHistory.getSpeed(SPEED_CALCULATION_INTERVAL) ?: Double.NaN

    fun getCapacity(speed: Double): Duration? =
        if(speed.isNaN()) null
        else Duration.ofSeconds(volume.div(speed).roundToLong())

    suspend fun estimateCurrentState(): Double {
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

    suspend fun estimateNextFullTimestamp(): Instant? {
        val start = emptyHistory.getLastEmptyTimestamp()
        if (start == null) return null
        val speed = stateHistory.getSpeed(SPEED_CALCULATION_INTERVAL)
        if (speed == null || speed <= 0.0) return null
        return start.plus(volume.div(speed).roundToLong(), ChronoUnit.SECONDS)
    }

    suspend fun getEmptingCountPerYear() =
        emptyHistory.getEmptingCountPerYear(EMPTING_CALCULATION_INTERVAL)

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