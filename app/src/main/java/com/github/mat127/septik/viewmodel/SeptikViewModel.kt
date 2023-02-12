package com.github.mat127.septik.viewmodel

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.mat127.septik.SeptikApplication
import com.github.mat127.septik.model.Septik
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

class SeptikViewModel(
    private val septik: Septik
) : ViewModel(), Septik.Observer {

    private val mutableLastEmptyTimestamp = MutableLiveData<Instant>(null)
    val lastEmptyTimestamp:LiveData<Instant> = mutableLastEmptyTimestamp

    private val mutableFillingSpeed = MutableLiveData<Double>(Double.NaN)
    val fillingSpeed:LiveData<Double> = mutableFillingSpeed

    private val mutableCapacity = MutableLiveData<Duration>(null)
    val capacity:LiveData<Duration> = mutableCapacity

    private val mutableCurrentState = MutableLiveData<Double>(Double.NaN)
    val currentState:LiveData<Double> = mutableCurrentState

    private val mutableCurrentPercent = MutableLiveData<Int>(-1)
    val currentPercent:LiveData<Int> = mutableCurrentPercent

    private val mutableNextFullTimestamp = MutableLiveData<Instant>(null)
    val nextFullDate:LiveData<Instant> = mutableNextFullTimestamp

    init {
        septik.addObserver(this)
        changed(septik)
    }

    // Define ViewModel factory in a companion object
    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return SeptikViewModel(
                    (application as SeptikApplication).septik
                ) as T
            }
        }
    }

    fun addEmptyTimestamp(timestamp: Instant) = viewModelScope.launch {
        septik.emptyHistory.add(timestamp)
    }

    fun addState(timestamp: Instant, state: Double) = viewModelScope.launch {
        septik.stateHistory.add(timestamp, state)
    }

    override fun changed(septik: Septik) {
        viewModelScope.launch {
            mutableLastEmptyTimestamp.value = septik.emptyHistory.getLastEmptyTimestamp()
            val speed = septik.getFillingSpeed()
            mutableFillingSpeed.value = speed
            mutableCapacity.value = septik.getCapacity(speed)
            val state = septik.estimateCurrentState()
            mutableCurrentState.value = state
            mutableCurrentPercent.value = septik.percent(state)
            mutableNextFullTimestamp.value = septik.estimateNextFullTimestamp()
        }
    }
}