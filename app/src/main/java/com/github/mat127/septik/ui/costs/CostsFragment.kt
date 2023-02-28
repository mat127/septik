package com.github.mat127.septik.ui.costs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mat127.septik.R
import com.github.mat127.septik.databinding.FragmentCostsBinding

class CostsFragment : Fragment() {

    private var _binding: FragmentCostsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val model: CostsViewModel by activityViewModels { CostsViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCostsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        model.emptingCountPerYear.observe(viewLifecycleOwner) { update() }
        model.emptingPrice.observe(viewLifecycleOwner) { update() }

        model.waterConsumptionPerDay.observe(viewLifecycleOwner) { updateWaterConsumption() }
        model.waterPrice.observe(viewLifecycleOwner) { updateWaterConsumption() }

        return root
    }

    private fun update() {
        updateEmptingCountPerYear(model.emptingCountPerYear.value)
        updateEmptingPrice(model.emptingPrice.value)
        updateEmptingPricePerYear(model.emptingCountPerYear.value, model.emptingPrice.value)
        updateEmptingPricePerMonth(model.emptingCountPerYear.value, model.emptingPrice.value)
    }

    private fun updateEmptingCountPerYear(emptingCount: Double?) {
        binding.textViewEmptingCountPerYear.text =
            if(emptingCount == null) getString(R.string.not_available)
            else String.format("%.02f", emptingCount)
    }

    private fun updateEmptingPrice(emptingPrice: Double?) {
        binding.textViewEmptingPrice.text =
            if(emptingPrice == null) getString(R.string.not_available)
            else String.format(getString(R.string.price_format), emptingPrice)
    }

    private fun updateEmptingPricePerYear(emptingCount: Double?, emptingPrice: Double?) {
        binding.textViewEmptingPricePerYear.text =
            if(emptingCount == null || emptingPrice == null) getString(R.string.not_available)
            else String.format(getString(R.string.price_format), emptingCount * emptingPrice)
    }

    private fun updateEmptingPricePerMonth(emptingCount: Double?, emptingPrice: Double?) {
        binding.textViewEmptingPricePerMonth.text =
            if(emptingCount == null || emptingPrice == null) getString(R.string.not_available)
            else String.format(getString(R.string.price_format), emptingCount * emptingPrice / 12)
    }

    private fun updateWaterConsumption() {
        updateWaterConsumptionPerDay(model.waterConsumptionPerDay.value)
        updateWaterPrice(model.waterPrice.value)
        updateWaterPricePerYear(model.waterConsumptionPerDay.value, model.waterPrice.value)
        updateWaterPricePerMonth(model.waterConsumptionPerDay.value, model.waterPrice.value)
    }

    private fun updateWaterConsumptionPerDay(consumption: Double?) {
        binding.textViewWaterConsumptionPerDay.text =
            if(consumption == null) getString(R.string.not_available)
            else String.format(getString(R.string.filling_speed_format), consumption)
    }

    private fun updateWaterPrice(price: Double?) {
        binding.textViewWaterPrice.text =
            if(price == null) getString(R.string.not_available)
            else String.format(getString(R.string.format_water_price), price)
    }

    private fun updateWaterPricePerYear(consumption: Double?, price: Double?) {
        binding.textViewWaterPricePerYear.text =
            if(consumption == null || price == null) getString(R.string.not_available)
            else String.format(getString(R.string.price_format), consumption * price * 365)
    }

    private fun updateWaterPricePerMonth(consumption: Double?, price: Double?) {
        binding.textViewWaterPricePerMonth.text =
            if(consumption == null || price == null) getString(R.string.not_available)
            else String.format(getString(R.string.price_format), consumption * price * 365/12)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}