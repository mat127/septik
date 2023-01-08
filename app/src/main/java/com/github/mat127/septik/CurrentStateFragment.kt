package com.github.mat127.septik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.mat127.septik.databinding.FragmentCurrentStateBinding
import com.github.mat127.septik.model.SeptikViewModel
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class CurrentStateFragment : Fragment() {

    private var _binding: FragmentCurrentStateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val model: SeptikViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.currentState.observe(viewLifecycleOwner, this::updateEstimatedState)
        model.currentPercent.observe(viewLifecycleOwner, this::updateEstimatedStatePercent)
        model.nextFullDate.observe(viewLifecycleOwner, this::updateNextFullDate)
    }

    private fun updateEstimatedState(state: Double) {
        binding.textViewFullness.text =
            if(state.isNaN()) getString(R.string.not_available)
            else String.format(getString(R.string.state_format), state)
    }

    private fun updateEstimatedStatePercent(percent: Int) {
        binding.textViewFullnessPercent.text =
            if(percent < 0) getString(R.string.not_available)
            else String.format(getString(R.string.state_percent_format), percent)
        binding.progressBarFullness.progress = percent
    }

    private fun updateNextFullDate(timestamp: LocalDateTime?) {
        if (timestamp == null) {
            binding.textViewFullDate.text = getString(R.string.not_available)
            binding.textViewFullDays.text = getString(R.string.not_available)
        }
        else {
            binding.textViewFullDate.text = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .format(timestamp)
            val duration = Duration.between(LocalDateTime.now(), timestamp)
            binding.textViewFullDays.text =
                String.format(getString(R.string.days_to_full_format), duration.toDays())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}