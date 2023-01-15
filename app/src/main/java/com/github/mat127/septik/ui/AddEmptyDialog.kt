package com.github.mat127.septik.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.github.mat127.septik.databinding.FragmentAddEmptyBinding
import com.github.mat127.septik.viewmodel.SeptikViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddEmptyDialog: DialogFragment() {

    private var _binding: FragmentAddEmptyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val septikViewModel: SeptikViewModel by activityViewModels { SeptikViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEmptyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        update()
        binding.chipDate.setOnClickListener {
            showDatePicker()
        }
        binding.chipTime.setOnClickListener {
            showTimePicker()
        }
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }
        binding.buttonOk.setOnClickListener {
            septikViewModel.addEmptyTimestamp(timestamp)
            dismiss()
        }
    }

    private var date = LocalDate.now()
    private var time = LocalTime.now()

    private val timestamp: Instant
        get() = LocalDateTime.of(date, time)
            .atZone(ZoneId.systemDefault())
            .toInstant()

    private fun update() {
        binding.chipDate.text = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            .format(date)
        binding.chipTime.text = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            .format(time)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}