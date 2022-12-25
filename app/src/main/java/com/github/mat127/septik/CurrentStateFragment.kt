package com.github.mat127.septik

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mat127.septik.databinding.FragmentCurrentStateBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CurrentStateFragment : Fragment() {

    private var _binding: FragmentCurrentStateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCurrentStateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}