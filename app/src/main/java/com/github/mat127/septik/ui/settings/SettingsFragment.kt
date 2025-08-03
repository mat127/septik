package com.github.mat127.septik.ui.settings

import android.os.Bundle
import android.text.InputType
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.github.mat127.septik.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        findPreference<EditTextPreference>(getString(R.string.preference_key_volume))?.let(::decimalNumberOnly)
        findPreference<EditTextPreference>(getString(R.string.preference_key_filling_estimation_interval))?.let(::numberOnly)
        findPreference<EditTextPreference>(getString(R.string.preference_key_costs_estimation_interval))?.let(::numberOnly)
        findPreference<EditTextPreference>(getString(R.string.preference_key_water_price))?.let(::decimalNumberOnly)
        findPreference<EditTextPreference>(getString(R.string.preference_key_empting_price))?.let(::decimalNumberOnly)
    }

    private fun numberOnly(preference: EditTextPreference) {
        preference.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    private fun decimalNumberOnly(preference: EditTextPreference) {
        preference.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
    }
}