package com.github.mat127.septik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mat127.septik.databinding.FragmentCurrentStateBinding
import com.github.mat127.septik.model.SeptikViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
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
        model.currentState.observe(viewLifecycleOwner, Observer { state ->
            binding.textViewFullness.text =
                if(state.isNaN()) "N/A" else String.format("%.4fm³", state)
        })
        model.currentPercent.observe(viewLifecycleOwner, Observer { percent ->
            binding.textViewFullnessPercent.text =
                if(percent < 0) "N/A" else String.format("%d%%", percent)
            binding.progressBarFullness.progress = percent
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}