package com.github.mat127.septik.ui.costs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CostsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is costs Fragment"
    }
    val text: LiveData<String> = _text
}