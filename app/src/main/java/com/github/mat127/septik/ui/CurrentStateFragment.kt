package com.github.mat127.septik.ui

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
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

    private val activity get() = super.getActivity() as MainActivity

    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrentStateBinding.inflate(inflater, container, false)
        rotateForward = AnimationUtils.loadAnimation(activity, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(activity, R.anim.rotate_backward)
        binding.addButton.setOnClickListener(this::openFab)
        binding.addStateButton.setOnClickListener { view ->
            closeFab(view)
            activity.addState()
        }
        binding.addEmptingButton.setOnClickListener { view ->
            closeFab(view)
            activity.addEmptyTimestamp()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.currentPercent.observe(viewLifecycleOwner, this::updateEstimatedStatePercent)
        model.nextFullDate.observe(viewLifecycleOwner, this::updateNextFullTimestamp)
        model.lastEmptyTimestamp.observe(viewLifecycleOwner, this::updateLastEmptyTimestamp)
        model.fillingSpeed.observe(viewLifecycleOwner, this::updateFillingSpeed)
        model.capacity.observe(viewLifecycleOwner, this::updateCapacity)
        model.currentState.observe(viewLifecycleOwner, this::updateEstimatedState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openFab(view: View) {
        binding.addButton.startAnimation(rotateForward)
        beginTransition(Fade.IN)
        showButton(binding.addStateButton, binding.addButton.id)
        showButton(binding.addEmptingButton, binding.addStateButton.id)
        binding.addButton.setOnClickListener(this::closeFab)
    }

    private fun closeFab(view: View) {
        binding.addButton.startAnimation(rotateBackward)
        beginTransition(Fade.OUT)
        hideButton(binding.addStateButton)
        hideButton(binding.addEmptingButton)
        binding.addButton.setOnClickListener(this::openFab)
    }

    private fun beginTransition(fadingMode: Int) {
        val transition = TransitionSet()
        transition.addTransition(Fade(fadingMode))
        transition.addTransition(ChangeBounds())
        transition.duration = 300
        TransitionManager.beginDelayedTransition(binding.currentStateLayout, transition)
    }

    private fun showButton(button: Button, bottomId: Int) {
        button.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomToTop = bottomId
        }
        button.visibility = ConstraintLayout.VISIBLE
        button.isClickable = true
    }

    private fun hideButton(button: Button) {
        button.updateLayoutParams<ConstraintLayout.LayoutParams> {
            bottomToTop = binding.tableLayout.id
        }
        button.visibility = ConstraintLayout.INVISIBLE
        button.isClickable = false
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