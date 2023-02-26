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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}