package com.github.mat127.septik.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

class SeptikViewModel : ViewModel(), Septik.Observer {

    val septik = Septik()

    init {
        septik.addObserver(this)
    }

    private val mutableCurrentState = MutableLiveData<Double>(Double.NaN)
    val currentState:LiveData<Double> = mutableCurrentState

    private val mutableCurrentPercent = MutableLiveData<Int>(-1)
    val currentPercent:LiveData<Int> = mutableCurrentPercent

    private val mutableNextFullDate = MutableLiveData<LocalDateTime>(null)
    val nextFullDate:LiveData<LocalDateTime> = mutableNextFullDate

    override fun changed(septik: Septik) {
        val state = septik.stateNow
        mutableCurrentState.value = state
        mutableCurrentPercent.value = septik.percent(state)
        mutableNextFullDate.value = septik.nextFullDate
    }
}