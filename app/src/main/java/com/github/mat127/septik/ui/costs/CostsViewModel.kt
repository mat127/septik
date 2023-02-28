package com.github.mat127.septik.ui.costs

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.github.mat127.septik.SeptikApplication
import com.github.mat127.septik.model.Septik
import kotlinx.coroutines.launch

class CostsViewModel(
    septik: Septik
) : ViewModel(), Septik.Observer {

    private val _emptingCountPerYear = MutableLiveData<Double>(Double.NaN)
    val emptingCountPerYear: LiveData<Double> = _emptingCountPerYear

    private val _emptingPrice = MutableLiveData<Double>(Double.NaN)
    val emptingPrice: LiveData<Double> = _emptingPrice

    private val _waterConsumptionPerDay = MutableLiveData<Double>(Double.NaN)
    val waterConsumptionPerDay: LiveData<Double> = _waterConsumptionPerDay

    private val _waterPrice = MutableLiveData<Double>(Double.NaN)
    val waterPrice: LiveData<Double> = _waterPrice

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
                return CostsViewModel(
                    (application as SeptikApplication).septik
                ) as T
            }
        }
    }

    override fun changed(septik: Septik) {
        viewModelScope.launch {
            _emptingCountPerYear.value = septik.getEmptingCountPerYear()
            _emptingPrice.value = septik.emptingPrice
            _waterConsumptionPerDay.value = septik.getWaterConsumption()
            _waterPrice.value = septik.waterPrice
        }
    }
}