package com.github.mat127.septik

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.github.mat127.septik.databinding.FragmentAddEmptyBinding
import com.github.mat127.septik.model.EmptyHistory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddEmptyDialog(private val history: EmptyHistory) : DialogFragment() {

    private var _binding: FragmentAddEmptyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
            history.add(timestamp)
            dismiss()
        }
    }

    var date = LocalDate.now()
    var time = LocalTime.now()

    val timestamp:LocalDateTime
        get() = LocalDateTime.of(date, time)

    private fun update() {
        binding.chipDate.setText(
            DateTimeFormatter.ISO_LOCAL_DATE.format(date)
        )
        val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        binding.chipTime.setText(
            formatter.format(time)
        )
    }

    private fun showDatePicker() {
        val dialog = DatePickerDialog(requireContext())
        dialog.setOnDateSetListener { datePicker, year, month, day ->
            date = LocalDate.of(year, month+1, day)
            update()
        }
        dialog.updateDate(date.year, date.monthValue-1, date.dayOfMonth)
        dialog.show()
    }

    private fun showTimePicker() {
        val dialog = TimePickerDialog(
            requireContext(),
            {  picker, hour, minute ->
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