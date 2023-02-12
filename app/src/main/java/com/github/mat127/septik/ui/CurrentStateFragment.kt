package com.github.mat127.septik.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.mat127.septik.R
import com.github.mat127.septik.databinding.FragmentCurrentStateBinding
import com.github.mat127.septik.viewmodel.SeptikViewModel
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CurrentStateFragment : Fragment() {

    private var _binding: FragmentCurrentStateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val model: SeptikViewModel by activityViewModels { SeptikViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.currentPercent.observe(viewLifecycleOwner, this::updateEstimatedStatePercent)
        model.nextFullDate.observe(viewLifecycleOwner, this::updateNextFullTimestamp)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateEstimatedStatePercent(percent: Int) {
        binding.textViewFullnessPercent.text =
            if(percent < 0) getString(R.string.not_available)
            else String.format(getString(R.string.state_percent_format), percent)
        binding.progressBarFullness.progress = percent
    }

    private fun updateNextFullTimestamp(timestamp: Instant?) {
        if (timestamp == null) {
            binding.textViewFullDate.text = getString(R.string.not_available)
            binding.textViewFullDays.text = getString(R.string.not_available)
        }
        else {
            val local = LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault())
            binding.textViewFullDate.text = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .format(local)
            val duration = Duration.between(Instant.now(), timestamp)
            binding.textViewFullDays.text =
                String.format(getString(R.string.capacity_format), duration.toDays())
        }
    }
}