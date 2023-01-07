package com.github.mat127.septik

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.github.mat127.septik.databinding.FragmentAddStateBinding
import com.github.mat127.septik.model.SeptikViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AddStateDialog : DialogFragment() {

    private var _binding: FragmentAddStateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val model: SeptikViewModel by activityViewModels()
    private val history get() = model.septik.stateHistory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddStateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update()
        binding.chipStateDate.setOnClickListener {
            showDatePicker()
        }
        binding.chipStateTime.setOnClickListener {
            showTimePicker()
        }
        binding.buttonCancel2.setOnClickListener {
            dismiss()
        }
        binding.buttonOk2.setOnClickListener {
            if(saveState())
                dismiss()
        }
    }

    private var date = LocalDate.now()
    private var time = LocalTime.now()

    private val timestamp: LocalDateTime
        get() = LocalDateTime.of(date, time)

    private fun update() {
        binding.chipStateDate.text = DateTimeFormatter.ISO_LOCAL_DATE.format(date)
        val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        binding.chipStateTime.text = formatter.format(time)
    }

    private fun showDatePicker() {
        val dialog = DatePickerDialog(requireContext())
        dialog.setOnDateSetListener { _, year, month, day ->
            date = LocalDate.of(year, month+1, day)
            update()
        }
        dialog.updateDate(date.year, date.monthValue-1, date.dayOfMonth)
        dialog.show()
    }

    private fun showTimePicker() {
        val dialog = TimePickerDialog(
            requireContext(),
            {  _, hour, minute ->
                time = LocalTime.of(hour, minute)
                update()
            },
            time.hour, time.minute, true
        )
        dialog.show()
    }

    private fun saveState(): Boolean {
        val state = binding.editTextState.text.toString().toDoubleOrNull()
        return if(state == null) {
            Toast.makeText(requireContext(), "State is empty or invalid", Toast.LENGTH_SHORT)
                .show()
            false
        }
        else {
            history.add(timestamp, state)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}