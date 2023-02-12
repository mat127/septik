package com.github.mat127.septik.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.mat127.septik.R
import com.github.mat127.septik.databinding.FragmentStatsBinding
import com.github.mat127.septik.viewmodel.SeptikViewModel
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val model: SeptikViewModel by activityViewModels { SeptikViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.lastEmptyTimestamp.observe(viewLifecycleOwner, this::updateLastEmptyTimestamp)
        model.fillingSpeed.observe(viewLifecycleOwner, this::updateFillingSpeed)
        model.capacity.observe(viewLifecycleOwner, this::updateCapacity)
        model.currentState.observe(viewLifecycleOwner, this::updateEstimatedState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateLastEmptyTimestamp(timestamp: Instant?) {
        if (timestamp == null) {
            binding.textViewLastEmptyDate.text = getString(R.string.not_available)
        }
        else {
            val local = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault())
            binding.textViewLastEmptyDate.text = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .format(local)
        }
    }

    private fun updateFillingSpeed(speed: Double) {
        binding.textViewFillingSpeed.text =
            if(speed.isNaN()) getString(R.string.not_available)
            else String.format(getString(R.string.filling_speed_format), speed * 24*60*60) // sec -> day
    }

    private fun updateCapacity(capacity: Duration?) {
        binding.textViewCapacity.text =
            if(capacity == null) getString(R.string.not_available)
            else String.format(getString(R.string.capacity_format), capacity.toDays())
    }

    private fun updateEstimatedState(state: Double) {
        binding.textViewFullness.text =
            if(state.isNaN()) getString(R.string.not_available)
            else String.format(getString(R.string.state_format), state)
    }
}